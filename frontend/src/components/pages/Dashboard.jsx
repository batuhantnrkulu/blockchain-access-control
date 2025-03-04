import React, { useState, useEffect } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Menu,
  MenuItem,
  Badge,
  Tabs,
  Tab,
  Box,
  styled,
} from "@mui/material";
import {
  Notifications as NotificationsIcon,
  AccountCircle,
  Logout,
} from "@mui/icons-material";
import BehaviorHistory from "./BehaviorHistory";
import AccessControlContracts from "./AccessControlContracts";
import Resources from "./Resources";
import "../../styles/Dashboard.css"; // CSS import for custom animations

// Import SockJS and StompJS for websocket connectivity.
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

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
  const [activeTab, setActiveTab] = useState("behaviorHistory");

  // Notifications state: each notification contains an id, message and a read flag.
  // For example: { id: 1, message: "New validator request", read: false }
  const [notifications, setNotifications] = useState([]);

  // Anchor states for the profile and notifications menus.
  const [anchorEl, setAnchorEl] = useState(null);
  const [notifAnchor, setNotifAnchor] = useState(null);

  // Compute unread notifications count.
  const unreadCount = notifications.filter((n) => !n.read).length;

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  // Open notifications menu and mark notifications as read.
  const handleNotificationsOpen = (event) => {
    setNotifAnchor(event.currentTarget);
    setNotifications((prevNotifs) =>
      prevNotifs.map((notif) => ({ ...notif, read: true }))
    );
  };

  const handleNotifMenuClose = () => {
    setNotifAnchor(null);
  };

  // ---------------------------
  // Connect to WebSocket server
  // ---------------------------
  useEffect(() => {
    // Create a SockJS connection to your backend endpoint.
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = new Client({
      // Use the SockJS instance for the connection.
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: function (str) {
        console.log(str);
      },
      onConnect: () => {
        // Subscribe to a topic based on the user.
        // Here we assume that the user object contains a 'bcAddress' property.
        // If not, you can use the username or adjust accordingly.
        const subscriptionDestination = `/topic/validator/${
          user.bcAddress || user.username
        }`;
        stompClient.subscribe(subscriptionDestination, (message) => {
          // Process the message. For simplicity, assume that the server sends text messages.
          const notificationMessage = message.body;
          const newNotification = {
            id: new Date().getTime(), // generate a unique id
            message: notificationMessage,
            read: false,
          };
          // Update state to include the new notification.
          setNotifications((prev) => [...prev, newNotification]);
        });
      },
    });

    stompClient.activate();

    // Clean up the connection on unmount.
    return () => {
      stompClient.deactivate();
    };
  }, [user]);

  return (
    <DashboardContainer className="fade-in">
      <AppBar position="static" color="default" elevation={1}>
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Welcome, {user.username}
          </Typography>

          <Box sx={{ display: "flex", alignItems: "center" }}>
            {/* Notifications Icon */}
            <IconButton
              color="inherit"
              size="large"
              className="notification-bell"
              onClick={handleNotificationsOpen}
            >
              <Badge badgeContent={unreadCount} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>

            {/* Profile Icon */}
            <IconButton
              color="inherit"
              size="large"
              onClick={handleProfileMenuOpen}
              sx={{ ml: 1 }}
            >
              <AccountCircle />
            </IconButton>
          </Box>
        </Toolbar>

        {/* Tabs */}
        <Tabs
          value={activeTab}
          onChange={handleTabChange}
          indicatorColor="primary"
          textColor="primary"
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
          <AccessControlContracts user={user} onLogout={onLogout} />
        )}
        {activeTab === "resources" && <Resources user={user} />}
      </MainContent>

      {/* Profile Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
      >
        <MenuItem onClick={onLogout}>
          <Logout sx={{ mr: 1 }} /> Logout
        </MenuItem>
      </Menu>

      {/* Notifications Menu */}
      <Menu
        anchorEl={notifAnchor}
        open={Boolean(notifAnchor)}
        onClose={handleNotifMenuClose}
      >
        {notifications.length > 0 ? (
          notifications.map((notif) => (
            <MenuItem key={notif.id} onClick={handleNotifMenuClose}>
              {notif.message}
            </MenuItem>
          ))
        ) : (
          <MenuItem onClick={handleNotifMenuClose}>No notifications</MenuItem>
        )}
      </Menu>
    </DashboardContainer>
  );
}

export default Dashboard;
