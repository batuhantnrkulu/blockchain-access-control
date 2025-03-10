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
} from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import api, { createBasicAuthHeader } from "../../utils/api";
import "../../styles/NotificationsPage.css";

const NotificationsPage = ({ user }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;

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

  const handlePrev = () => {
    if (page > 0) setPage(page - 1);
  };

  const handleNext = () => {
    if (page < totalPages - 1) setPage(page + 1);
  };

  // Mark notification as read
  const handleNotificationClick = async (notificationId) => {
    try {
      await api.put(`/api/notifications/${notificationId}/read`, null, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      // Update state to reflect notification as read
      setNotifications((prevNotifs) =>
        prevNotifs.map((notif) =>
          notif.id === notificationId ? { ...notif, read: true } : notif
        )
      );
    } catch (error) {
      console.error("Error marking notification as read", error);
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
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          className="notifications-page-loading"
        >
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
                onClick={() => handleNotificationClick(notification.id)}
                sx={{
                  backgroundColor: notification.read ? "#f0f0f0" : "#d1e7fd", // Unread: Blue | Read: Gray
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
            <Typography
              variant="body1"
              className="no-notifications"
              align="center"
            >
              No notifications available.
            </Typography>
          )}
        </List>
      )}

      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        className="pagination-container"
      >
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
    </Container>
  );
};

export default NotificationsPage;
