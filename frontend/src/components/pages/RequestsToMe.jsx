import React, { useState, useEffect } from "react";
import {
  Table,
  TableContainer,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
  Button,
  Box,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControlLabel,
  Checkbox,
  TablePagination,
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import { useLocation, useNavigate } from "react-router-dom";
import api, { createBasicAuthHeader } from "../../utils/api";

const RequestsToMe = ({ user, navigationState }) => {
  const [requestsToMe, setRequestsToMe] = useState([]);
  const [policyModalOpen, setPolicyModalOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [myResources, setMyResources] = useState([]);
  const [selectedResource, setSelectedResource] = useState(null);
  const [policySettings, setPolicySettings] = useState({
    view: false,
    edit: false,
    delete: false,
  });
  const [resourceSearch, setResourceSearch] = useState("");
  const [resourcePage, setResourcePage] = useState(0);
  const [resourceRowsPerPage, setResourceRowsPerPage] = useState(10);
  const [resourceTotalElements, setResourceTotalElements] = useState(0);
  const [expandedResourceId, setExpandedResourceId] = useState(null);
  const [hasProcessedInitialState, setHasProcessedInitialState] =
    useState(false);

  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    if (navigationState?.accId && navigationState?.resourceName) {
      // Once requests are loaded, find the matching request
      if (requestsToMe.length === 0) return; // Exit if data isn't loaded

      const accId = parseInt(navigationState.accId, 10);
      const matchingRequest = requestsToMe.find((req) => req.id === accId);

      if (matchingRequest) {
        setSelectedRequest(matchingRequest);
        setResourceSearch(navigationState.resourceName);
        setPolicyModalOpen(true);
        setHasProcessedInitialState(true);
      } else {
        console.error("No request found for ACC ID:", accId);
        setPolicyModalOpen(false); // Close modal if no match
      }
    }
  }, [navigationState, requestsToMe]);

  useEffect(() => {
    if (navigationState?.accId && navigationState?.resourceName) {
      setResourceSearch(navigationState.resourceName);
      setHasProcessedInitialState(false); // Reset this so the new useEffect can process
    }
  }, [navigationState]);

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

      const sortedRequests = response.data.sort((a, b) => {
        return a.approved === b.approved ? 0 : a.approved ? 1 : -1;
      });

      setRequestsToMe(sortedRequests);
    } catch (err) {
      console.error("Error fetching requests to me", err);
    }
  };

  const handleApproveRequest = async (request) => {
    const accType = "ACCESS";
    try {
      const response = await api.post(
        `/api/access-request/approve/${request.id}`,
        null,
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
          params: { accType },
        }
      );

      alert("Access approved! ACC deployed at: " + response.data);

      setRequestsToMe((prevRequests) =>
        prevRequests.map((r) =>
          r.id === request.id
            ? { ...r, approved: true, accAddress: response.data }
            : r
        )
      );
    } catch (error) {
      console.error("Error approving access request", error);
      alert("Failed to approve access request.");
    }
  };

  const openPolicyModal = (req) => {
    setSelectedRequest(req);
    fetchMyResources();
    setPolicyModalOpen(true);
  };

  const fetchMyResources = async () => {
    try {
      const response = await api.get("/api/resources", {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
        params: {
          username: user.username,
          search: resourceSearch || "",
          page: resourcePage,
          size: resourceRowsPerPage,
        },
      });

      setMyResources(response.data.content || []);
      setResourceTotalElements(
        response.data.totalElements || response.data.length
      );
    } catch (error) {
      console.error("Error fetching my resources:", error);
    }
  };

  const handlePolicyModalClose = () => {
    setResourceSearch("");
    setPolicyModalOpen(false);
    setSelectedRequest(null);
    setHasProcessedInitialState(false);
    navigate(location.pathname, { replace: true });
  };

  const fetchPolicyStatus = async (resource) => {
    try {
      const res = await api.get("/api/policies/status", {
        params: {
          accessRequestId: selectedRequest.id,
          resourceId: resource.id,
        },
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      setPolicySettings({
        view: res.data.view === "allowed",
        edit: res.data.edit === "allowed",
        delete: res.data.delete === "allowed",
      });
    } catch (error) {
      console.error("Error fetching policy status:", error);
    }
  };

  const handlePolicySubmit = async () => {
    if (!selectedResource) {
      alert("Please select a resource.");
      return;
    }

    const payload = {
      accessRequestId: selectedRequest.id,
      resourceId: selectedResource.id,
      view: policySettings.view ? "allowed" : "disallowed",
      edit: policySettings.edit ? "allowed" : "disallowed",
      delete: policySettings.delete ? "allowed" : "disallowed",
    };

    try {
      await api.post("/api/policies/update-bulk", payload, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });
      alert("Policy updated successfully!");
      setPolicyModalOpen(false);
    } catch (error) {
      console.error("Error updating policies", error);
      alert("Failed to update policies.");
    }
  };

  const toggleExpandResource = (resource) => {
    setExpandedResourceId((prev) =>
      prev === resource.id ? null : resource.id
    );

    if (expandedResourceId !== resource.id) {
      setSelectedResource(resource);
      fetchPolicyStatus(resource);
    }
  };

  useEffect(() => {
    fetchRequestsToMe();
  }, [user]);

  useEffect(() => {
    fetchMyResources();
  }, [resourceSearch, resourcePage, resourceRowsPerPage, user]);

  return (
    <Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Subject</TableCell>
              <TableCell>Requested Access</TableCell>
              <TableCell>Approval Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {requestsToMe.length > 0 ? (
              requestsToMe.map((req, i) => (
                <TableRow key={i}>
                  <TableCell>{req.subjectPeerName}</TableCell>
                  <TableCell>{req.objectPeerName}</TableCell>
                  <TableCell>
                    {!req.approved ? (
                      <Button
                        variant="contained"
                        color="success"
                        size="small"
                        onClick={() => handleApproveRequest(req)}
                      >
                        Approve
                      </Button>
                    ) : (
                      <>
                        <Box display="flex" alignItems="center" color="green">
                          <CheckCircleIcon sx={{ fontSize: 20, mr: 1 }} />
                          <Typography variant="body2">
                            Approved - ACC Address: {req.accAddress || "N/A"}
                          </Typography>
                        </Box>
                        <Button
                          variant="outlined"
                          color="primary"
                          size="small"
                          onClick={() => openPolicyModal(req)}
                          sx={{ ml: 1 }}
                        >
                          Configure Policies
                        </Button>
                      </>
                    )}
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={3}>
                  <Typography align="center">No requests found.</Typography>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog
        open={policyModalOpen}
        onClose={handlePolicyModalClose}
        fullWidth
        maxWidth="md"
      >
        <DialogTitle>Configure Policies for Request</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            label="Search Resource"
            variant="outlined"
            value={resourceSearch}
            onChange={(e) => {
              setResourceSearch(e.target.value);
              setResourcePage(0);
            }}
            sx={{ mb: 2 }}
          />

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Resource Name</TableCell>
                  <TableCell>Type</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {myResources.map((resource) => (
                  <React.Fragment key={resource.id}>
                    <TableRow>
                      <TableCell>{resource.id}</TableCell>
                      <TableCell>{resource.resourceName}</TableCell>
                      <TableCell>{resource.type}</TableCell>
                      <TableCell>
                        <Button
                          variant="outlined"
                          color="primary"
                          onClick={() => toggleExpandResource(resource)}
                        >
                          {expandedResourceId === resource.id
                            ? "▲ Hide"
                            : "▼ Show"}
                        </Button>
                      </TableCell>
                    </TableRow>

                    {expandedResourceId === resource.id && (
                      <TableRow>
                        <TableCell colSpan={4}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={policySettings.view}
                                onChange={(e) =>
                                  setPolicySettings((prev) => ({
                                    ...prev,
                                    view: e.target.checked,
                                  }))
                                }
                              />
                            }
                            label="View"
                          />
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={policySettings.edit}
                                onChange={(e) =>
                                  setPolicySettings((prev) => ({
                                    ...prev,
                                    edit: e.target.checked,
                                  }))
                                }
                              />
                            }
                            label="Edit"
                          />
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={policySettings.delete}
                                onChange={(e) =>
                                  setPolicySettings((prev) => ({
                                    ...prev,
                                    delete: e.target.checked,
                                  }))
                                }
                              />
                            }
                            label="Delete"
                          />
                        </TableCell>
                      </TableRow>
                    )}
                  </React.Fragment>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <TablePagination
            component="div"
            count={resourceTotalElements}
            page={resourcePage}
            onPageChange={(event, newPage) => setResourcePage(newPage)}
            rowsPerPage={resourceRowsPerPage}
            onRowsPerPageChange={(event) => {
              setResourceRowsPerPage(parseInt(event.target.value, 10));
              setResourcePage(0);
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handlePolicyModalClose}>Cancel</Button>
          <Button
            onClick={handlePolicySubmit}
            variant="contained"
            color="primary"
          >
            Submit
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default RequestsToMe;
