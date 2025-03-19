import React, { useState, useEffect } from "react";
import { Box, Tabs, Tab, Typography } from "@mui/material";
import { styled } from "@mui/system";
import OtherPeersResources from "./OtherPeersResources";
import RequestsToMe from "./RequestsToMe";
import { useLocation } from "react-router-dom";

const Container = styled(Box)(({ theme }) => ({
  padding: theme.spacing(3),
  marginTop: theme.spacing(2),
}));

function AccessControlContracts({ user, onLogout, navigationState }) {
  const [activeTab, setActiveTab] = useState("other-resources");
  const location = useLocation();

  useEffect(() => {
    if (navigationState?.activeTab === "requests-to-me") {
      setActiveTab("requests-to-me");
      window.history.replaceState({}, document.title);
    }
  }, [navigationState]);

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Access Control Contracts
      </Typography>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs value={activeTab} onChange={(e, newVal) => setActiveTab(newVal)}>
          <Tab label="Other Peers' Resources" value="other-resources" />
          <Tab label="Requests To Me" value="requests-to-me" />
        </Tabs>
      </Box>
      <Container>
        {activeTab === "other-resources" && <OtherPeersResources user={user} />}
        {activeTab === "requests-to-me" && (
          <RequestsToMe user={user} navigationState={navigationState} />
        )}
      </Container>
    </Box>
  );
}

export default AccessControlContracts;
