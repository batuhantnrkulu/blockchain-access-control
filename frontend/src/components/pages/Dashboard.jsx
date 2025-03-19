import React, { useState, useEffect } from "react";
import {
  AppBar,
  Toolbar,
  Chip,
  Typography,
  IconButton,
  Menu,
  MenuItem,
  Badge,
  Tabs,
  Tab,
  Box,
  styled,
  Button,
  Divider,
} from "@mui/material";
import {
  Notifications as NotificationsIcon,
  AccountCircle,
  Logout,
} from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import BehaviorHistory from "./BehaviorHistory";
import AccessControlContracts from "./AccessControlContracts";
import Resources from "./Resources";
import api, { createBasicAuthHeader } from "../../utils/api";
import "../../styles/Dashboard.css";

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

import { useLocation } from "react-router-dom";

const DashboardContainer = styled("div")({
  display: "flex",
  flexDirection: "column",
  minHeight: "100vh",
});

const MainContent = styled("main")(({ theme }) => ({
  flex: 1,
  padding: theme.spacing(3),
  backgroundColor: theme.palette.background.default,
}));

function Dashboard({ user, onLogout }) {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("behaviorHistory");
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [anchorEl, setAnchorEl] = useState(null);
  const [notifAnchor, setNotifAnchor] = useState(null);
  const location = useLocation();
  const navigationState = location.state || {}; // Handle case where no state is passed

  useEffect(() => {
    if (navigationState.activeTab) {
      setActiveTab("accessContracts");
      // Clear all navigation state properties
      window.history.replaceState({}, document.title);
    }
  }, [navigationState]);

  // 🔹 **Fetch Unread Notifications (Only Once on Load)**
  const fetchUnreadNotifications = async () => {
    try {
      const response = await api.get("/api/notifications", {
        params: { username: user.username, page: 0, size: 5 },
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      const unreadNotifications = response.data.content.filter(
        (notif) => !notif.read
      );
      console.log(response);
      setNotifications(unreadNotifications);
      setUnreadCount(unreadNotifications.length);
    } catch (error) {
      console.error("Error fetching unread notifications:", error);
    }
  };

  // 🔹 **Fetch Notifications Only When Dashboard Loads**
  useEffect(() => {
    if (user) {
      fetchUnreadNotifications();
    }
  }, [user]);

  // 🔹 **WebSocket for Live Notifications**
  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      onConnect: () => {
        const subscriptionDestination = `/topic/validator/${
          user.memberAddress || user.username
        }`;
        stompClient.subscribe(subscriptionDestination, (message) => {
          const newNotification = {
            id: new Date().getTime(),
            message: message.body,
            read: false,
          };

          setNotifications((prev) => [...prev, newNotification]);
          setUnreadCount((prevCount) => prevCount + 1);
        });
      },
    });

    stompClient.activate();
    return () => stompClient.deactivate();
  }, [user]);

  // 🔹 **Open Notifications Menu - DO NOT REFETCH ALL**
  const handleNotificationsOpen = (event) => {
    setNotifAnchor(event.currentTarget);
  };

  const handleNotifMenuClose = () => {
    setNotifAnchor(null);
  };

  // 🔹 **Mark Notification as Read**
  const handleNotificationClick = async (notificationId) => {
    try {
      await api.put(`/api/notifications/${notificationId}/read`, null, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      // ✅ **Remove Read Notification from Bell Without Reloading**
      setNotifications((prevNotifs) =>
        prevNotifs.filter((notif) => notif.id !== notificationId)
      );

      // ✅ **Update Unread Count**
      setUnreadCount((prevCount) => (prevCount > 0 ? prevCount - 1 : 0));
    } catch (error) {
      console.error("Error marking notification as read", error);
    }
  };

  // 🔹 **Navigate to Full Notifications Page**
  const handleViewAllNotifications = () => {
    handleNotifMenuClose();
    navigate("/notifications", { state: { reload: true } });
  };

  return (
    <DashboardContainer className="fade-in">
      <AppBar position="static" color="default" elevation={1}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Welcome, {user.memberAddress}
            <Chip
              label={`Peer's Current Status: ${user.status}`}
              sx={{
                ml: 2,
                backgroundColor:
                  user.status === "BENIGN"
                    ? "#4CAF50"
                    : user.status === "SUSPICIOUS"
                    ? "#FF9800"
                    : "#F44336",
                color: "white",
              }}
            />
          </Typography>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <IconButton
              color="inherit"
              size="large"
              onClick={handleNotificationsOpen}
            >
              <Badge badgeContent={unreadCount} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>

            <IconButton
              color="inherit"
              size="large"
              onClick={(e) => setAnchorEl(e.currentTarget)}
              sx={{ ml: 1 }}
            >
              <AccountCircle />
            </IconButton>
          </Box>
        </Toolbar>
        <Tabs
          value={activeTab}
          onChange={(event, newValue) => setActiveTab(newValue)}
          centered
        >
          <Tab label="Behavior History" value="behaviorHistory" />
          <Tab label="Access Control Contracts" value="accessContracts" />
          <Tab label="Resources" value="resources" />
        </Tabs>
      </AppBar>

      <MainContent>
        {activeTab === "behaviorHistory" && <BehaviorHistory user={user} />}
        {activeTab === "accessContracts" && (
          <AccessControlContracts
            user={user}
            onLogout={onLogout}
            navigationState={navigationState}
          />
        )}
        {activeTab === "resources" && <Resources user={user} />}
      </MainContent>

      {/* Profile Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => setAnchorEl(null)}
      >
        <MenuItem onClick={onLogout}>
          <Logout sx={{ mr: 1 }} /> Logout
        </MenuItem>
      </Menu>

      {/* Notifications Menu - Show Only Unread */}
      <Menu
        anchorEl={notifAnchor}
        open={Boolean(notifAnchor)}
        onClose={handleNotifMenuClose}
        PaperProps={{ sx: { width: 300 } }}
      >
        {notifications.length > 0 ? (
          <>
            {notifications.map((notif) => (
              <MenuItem
                key={notif.id}
                onClick={() => handleNotificationClick(notif.id)}
              >
                {notif.message}
              </MenuItem>
            ))}
            <Divider />
            <MenuItem
              onClick={handleViewAllNotifications}
              sx={{ display: "flex", justifyContent: "center" }}
            >
              <Button fullWidth color="primary" variant="contained">
                View All Notifications
              </Button>
            </MenuItem>
          </>
        ) : (
          <>
            <MenuItem onClick={handleNotifMenuClose}>
              No unread notifications
            </MenuItem>
            <Divider />
            <MenuItem
              onClick={handleViewAllNotifications}
              sx={{ display: "flex", justifyContent: "center" }}
            >
              <Button fullWidth color="primary" variant="contained">
                View All Notifications
              </Button>
            </MenuItem>
          </>
        )}
      </Menu>
    </DashboardContainer>
  );
}

export default Dashboard;
