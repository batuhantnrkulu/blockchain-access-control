// src/components/AccessControlContracts.js
import React, { useState, useEffect } from "react";
import { Box, Tabs, Tab, Grid, Paper, Typography, Chip } from "@mui/material";
import { styled } from "@mui/system";
import api, { createBasicAuthHeader } from "../../utils/api";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const RequestContainer = styled(Box)(({ theme }) => ({
  padding: theme.spacing(3),
  marginTop: theme.spacing(2),
}));

const RequestCard = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  marginBottom: theme.spacing(2),
  transition: "box-shadow 0.3s",
  "&:hover": {
    boxShadow: theme.shadows[4],
  },
}));

function AccessControlContracts({ user, onLogout }) {
  const [activeRequestTab, setActiveRequestTab] = useState("my-requests");
  const [notifications, setNotifications] = useState([]);
  const [myRequests, setMyRequests] = useState([]);
  const [requestsToMe, setRequestsToMe] = useState([]);

  useEffect(() => {
    // Fetch access-request data
    fetchMyRequests();
    fetchRequestsToMe();

    // Set up SockJS + STOMP for notifications
    const socket = new SockJS("http://localhost:8080/ws", null, {
      transports: ["websocket", "xhr-streaming", "xhr-polling"],
      headers: {
        Authorization: createBasicAuthHeader(user.username, user.password),
      },
    });

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: createBasicAuthHeader(user.username, user.password),
      },
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("WebSocket connected");
        stompClient.subscribe(
          `/topic/notifications/${user.bcAddress}`,
          (message) => {
            setNotifications((prev) => [...prev, JSON.parse(message.body)]);
          }
        );
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers.message);
      },
      debug: (str) => console.log("STOMP:", str),
    });

    stompClient.activate();
    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.deactivate();
      }
    };
  }, [user]);

  const fetchMyRequests = async () => {
    try {
      const response = await api.get(
        `api/access-request/my-requests?username=${user.username}`,
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        }
      );
      setMyRequests(response.data);
    } catch (err) {
      console.error("Error fetching my requests", err);
    }
  };

  const fetchRequestsToMe = async () => {
    try {
      const response = await api.get(
        `api/access-request/requests-to-me?username=${user.username}`,
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        }
      );
      setRequestsToMe(response.data);
    } catch (err) {
      console.error("Error fetching requests to me", err);
    }
  };

  return (
    <Box>
      {/* Tab Header for Requests */}
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs
          value={activeRequestTab}
          onChange={(e, newVal) => setActiveRequestTab(newVal)}
        >
          <Tab label="Requests I Created" value="my-requests" />
          <Tab label="Requests To Me" value="requests-to-me" />
        </Tabs>
      </Box>

      {/* Content Section */}
      <RequestContainer>
        {activeRequestTab === "my-requests" ? (
          <Grid container spacing={2}>
            {myRequests.map((req, i) => (
              <Grid item xs={12} md={6} key={i}>
                <RequestCard elevation={2}>
                  <Typography variant="subtitle1">
                    Object: {req.objectPeerName}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Subject: {req.subjectPeerName}
                  </Typography>
                  <Chip
                    label={req.approved ? "Approved" : "Pending"}
                    color={req.approved ? "success" : "warning"}
                    sx={{ mt: 1 }}
                  />
                </RequestCard>
              </Grid>
            ))}
          </Grid>
        ) : (
          <Grid container spacing={2}>
            {requestsToMe.map((req, i) => (
              <Grid item xs={12} md={6} key={i}>
                <RequestCard elevation={2}>
                  <Typography variant="subtitle1">
                    Subject: {req.subjectPeerName}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Requested Access: {req.objectPeerName}
                  </Typography>
                  <Chip
                    label={req.approved ? "Approved" : "Pending"}
                    color={req.approved ? "success" : "warning"}
                    sx={{ mt: 1 }}
                  />
                </RequestCard>
              </Grid>
            ))}
          </Grid>
        )}
      </RequestContainer>
    </Box>
  );
}

export default AccessControlContracts;
