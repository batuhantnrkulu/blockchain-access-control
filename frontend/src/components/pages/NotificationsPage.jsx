import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
  CircularProgress,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import api, { createBasicAuthHeader } from "../../utils/api";
import "../../styles/NotificationsPage.css";

const NotificationsPage = ({ user }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [responseStatus, setResponseStatus] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;

  // Popup States
  const [selectedNotification, setSelectedNotification] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [peerDetails, setPeerDetails] = useState(null);

  const [selectedAccId, setSelectedAccId] = useState(null);
  const [selectedResourceName, setSelectedResourceName] = useState("");

  // Fetch notifications from API
  const fetchNotifications = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/notifications", {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
        params: { username: user.username, page, size: pageSize },
      });

      setNotifications(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } catch (error) {
      console.error("Error fetching notifications", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchNotifications();
  }, [page, user]); // Ensure the notifications are fetched when the user or page changes

  // Extract ID from notification message (Assumes format: "Message text (ID)")
  const extractIdFromMessage = (message) => {
    const match = message.match(/\((\d+)\)$/); // Extracts last number in parentheses
    return match ? match[1] : null;
  };

  // Fetch Unjoined Peer details
  const fetchPeerDetails = async (id) => {
    try {
      const res = await api.get(`/api/role-token/unjoined-peer/${id}`, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });
      setPeerDetails(res.data);
    } catch (error) {
      console.error("Error fetching peer details", error);
      setPeerDetails(null);
    }
  };

  const handlePrev = () => {
    if (page > 0) setPage(page - 1);
  };

  const handleNext = () => {
    if (page < totalPages - 1) setPage(page + 1);
  };

  // Mark notification as read
  // Open popup when a notification is clicked
  const handleNotificationClick = async (notification) => {
    setSelectedNotification(notification);
    setDialogOpen(true);
    setResponseStatus(null); // Reset status message

    if (notification.type === "VALIDATION_REQUEST") {
      const peerId = extractIdFromMessage(notification.message);

      if (peerId) {
        fetchPeerDetails(peerId);

        try {
          // ✅ Call backend to check if expired or already responded
          const response = await api.get(
            "/api/validator-response/check-response",
            {
              headers: {
                Authorization: createBasicAuthHeader(
                  user.username,
                  user.password
                ),
              },
              params: {
                unjoinedPeerId: peerId,
                validatorId: user.id, // Validator ID (logged-in user)
              },
            }
          );

          if (response.data === false) {
            setResponseStatus(null);
          } else if (response.data === true) {
            setResponseStatus("You have already responded to this request.");
          } else {
            setResponseStatus("validation request not found"); // Allow validation
          }
        } catch (error) {
          console.error("Error checking response status", error);
          setResponseStatus("This validation request has expired.");
        }
      }
    }

    if (notification.type === "POLICY_REQUEST") {
      try {
        // Extract ACC ID and Resource Name from notification message
        const accMatch = notification.message.match(/ACC\((\d+)\)/);
        const resourceMatch = notification.message.match(/Resource\((.*?)\)/);

        if (accMatch) setSelectedAccId(accMatch[1]); // Extract ACC ID
        if (resourceMatch) setSelectedResourceName(resourceMatch[1]); // Extract Resource Name
      } catch (error) {
        console.error("Error parsing POLICY_REQUEST notification", error);
      }
    }

    // ✅ Mark notification as read
    try {
      await api.put(`/api/notifications/${notification.id}/read`, null, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      setNotifications((prevNotifs) =>
        prevNotifs.map((notif) =>
          notif.id === notification.id ? { ...notif, read: true } : notif
        )
      );
    } catch (error) {
      console.error("Error marking notification as read", error);
    }
  };

  // ✅ Handle "Approve" or "Reject" Validator Response
  const handleValidatorResponse = async (approved) => {
    try {
      await api.post(
        "/api/validator-response",
        {
          unjoinedPeerId: extractIdFromMessage(selectedNotification.message), // Extracted ID
          validatorBcAddress: user.memberAddress, // Get validator address from user
          approved: approved,
        },
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        }
      );

      alert(`Validation request ${approved ? "approved" : "rejected"}!`);
      setDialogOpen(false);
      fetchNotifications();
    } catch (error) {
      console.error("Error submitting validator response", error);
      alert("Failed to process the response.");
    }
  };

  return (
    <Container className="notifications-page-container">
      <Typography
        variant="h4"
        gutterBottom
        className="notifications-page-title"
      >
        Notifications
      </Typography>

      {loading ? (
        <Box display="flex" justifyContent="center" alignItems="center">
          <CircularProgress />
        </Box>
      ) : (
        <List className="notifications-list">
          {notifications.length > 0 ? (
            notifications.map((notification) => (
              <ListItem
                key={notification.id}
                divider
                button
                onClick={() => handleNotificationClick(notification)}
                sx={{
                  backgroundColor: notification.read ? "#f0f0f0" : "#d1e7fd",
                  "&:hover": {
                    backgroundColor: notification.read ? "#e0e0e0" : "#bcd4f2",
                  },
                }}
              >
                <ListItemText
                  primary={notification.message}
                  secondary={`${notification.type} - ${notification.createdAt}`}
                />
              </ListItem>
            ))
          ) : (
            <Typography variant="body1" align="center">
              No notifications available.
            </Typography>
          )}
        </List>
      )}

      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Button variant="contained" onClick={handlePrev} disabled={page === 0}>
          Previous
        </Button>
        <Typography variant="body2">
          Page {page + 1} of {totalPages}
        </Typography>
        <Button
          variant="contained"
          onClick={handleNext}
          disabled={page >= totalPages - 1}
        >
          Next
        </Button>
      </Box>

      <Box mt={2} textAlign="center">
        <Button
          variant="text"
          color="primary"
          onClick={() => navigate("/dashboard")}
        >
          Back to Dashboard
        </Button>
      </Box>

      {/* ✅ Notification Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>
          {selectedNotification && (
            <>
              <Typography variant="body1">
                {selectedNotification.message}
              </Typography>
              <Typography variant="caption" color="textSecondary">
                {selectedNotification.type} - {selectedNotification.createdAt}
              </Typography>

              {/* ✅ Show Peer Details for Validation Request */}
              {selectedNotification.type === "VALIDATION_REQUEST" &&
                peerDetails && (
                  <Box
                    mt={2}
                    p={2}
                    sx={{ border: "1px solid #ccc", borderRadius: 2 }}
                  >
                    <Typography variant="subtitle1">
                      Unjoined Peer Details:
                    </Typography>
                    <Typography variant="body2">
                      <strong>Username:</strong> {peerDetails.username}
                    </Typography>
                    <Typography variant="body2">
                      <strong>BC Address:</strong> {peerDetails.bcAddress}
                    </Typography>
                    <Typography variant="body2">
                      <strong>Usage Purpose:</strong> {peerDetails.usagePurpose}
                    </Typography>
                    <Typography variant="body2">
                      <strong>Group:</strong> {peerDetails.group}
                    </Typography>
                  </Box>
                )}

              {/* Show Approve & Reject buttons for Validation Requests */}
              {selectedNotification.type === "VALIDATION_REQUEST" &&
              responseStatus ? (
                <Typography variant="body1" color="error" sx={{ mt: 2 }}>
                  {responseStatus}
                </Typography>
              ) : (
                selectedNotification.type === "VALIDATION_REQUEST" && (
                  <Box mt={2} display="flex" justifyContent="space-between">
                    <Button
                      variant="contained"
                      color="success"
                      onClick={() => handleValidatorResponse(true)}
                    >
                      Approve
                    </Button>
                    <Button
                      variant="contained"
                      color="error"
                      onClick={() => handleValidatorResponse(false)}
                    >
                      Reject
                    </Button>
                  </Box>
                )
              )}
              {selectedNotification?.type === "POLICY_REQUEST" &&
                selectedAccId && (
                  <Box mt={2}>
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => {
                        navigate("/dashboard", {
                          state: {
                            accId: selectedAccId,
                            resourceName: selectedResourceName,
                            activeTab: "requests-to-me",
                            fromNotification: true, // Add flag
                          },
                          replace: true, // Prevent adding to history stack
                        });
                      }}
                    >
                      Configure Policies for ACC ({selectedAccId})
                    </Button>
                  </Box>
                )}
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default NotificationsPage;
