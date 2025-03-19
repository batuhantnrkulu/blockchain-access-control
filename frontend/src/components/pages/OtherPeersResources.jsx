import React, { useState, useEffect } from "react";
import {
  TextField,
  Table,
  TableContainer,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Radio,
  RadioGroup,
  Paper,
  TablePagination,
  Button,
  Chip,
  IconButton,
  Collapse,
  Box,
  Typography,
  FormControlLabel,
  Checkbox,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
} from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import api, { createBasicAuthHeader } from "../../utils/api";

const OtherPeersResources = ({ user }) => {
  const [otherPeersResources, setOtherPeersResources] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [expandedPermissionResourceId, setExpandedPermissionResourceId] =
    useState(null);
  const [permissionRequestSettings, setPermissionRequestSettings] = useState({
    view: false,
    edit: false,
    delete: false,
  });
  const [selectedResource, setSelectedResource] = useState(null);
  const [viewOpen, setViewOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [resourceForm, setResourceForm] = useState({
    resourceName: "",
    type: "jpg",
    file: null,
    dataResource: "",
  });
  const [loading, setLoading] = useState(false);

  const [blockedDialogOpen, setBlockedDialogOpen] = useState(false);
  const [blockEndTime, setBlockEndTime] = useState(null);
  const [timeLeft, setTimeLeft] = useState("");

  const fetchOtherPeersResources = async () => {
    try {
      const response = await api.get(`/api/access-request/other-resources`, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
        params: {
          peerId: user.id,
          search: search,
          page: page,
          size: rowsPerPage,
        },
      });

      setOtherPeersResources(response.data.content);
      setTotalElements(response.data.totalElements);
    } catch (err) {
      console.error("Error fetching other peers' resources", err);
    }
  };

  // Add this effect for updating the timer
  useEffect(() => {
    let interval;
    if (blockedDialogOpen && blockEndTime) {
      interval = setInterval(() => {
        const now = new Date();
        const end = new Date(blockEndTime);
        const diff = end - now;

        if (diff <= 0) {
          setBlockedDialogOpen(false);
          clearInterval(interval);
          return;
        }

        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor(
          (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        );
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((diff % (1000 * 60)) / 1000);

        setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [blockedDialogOpen, blockEndTime]);

  // Add this check function
  const checkBlockStatus = async () => {
    try {
      const response = await api.get(
        `/api/peers/${user.memberAddress}/is-blocked`,
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        }
      );

      if (response.data.blocked) {
        setBlockEndTime(response.data.blockingEndTime);
        setBlockedDialogOpen(true);
        return true;
      }
      return false;
    } catch (error) {
      console.error("Block check failed:", error);
      return false;
    }
  };

  const handleRequestAccess = async (resource) => {
    if (await checkBlockStatus()) return;
    try {
      const payload = {
        objectPeerId: resource.ownerPeerId,
        subjectPeerId: user.id,
      };

      await api.post("/api/access-request/create", payload, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      setOtherPeersResources((prevResources) =>
        prevResources.map((r) =>
          r.resourceId === resource.resourceId
            ? { ...r, accessControlStatus: "Pending" }
            : r
        )
      );
    } catch (error) {
      console.error("Error requesting access", error);
      alert("Failed to request access.");
    }
  };

  const checkResourceAccess = async (resource, action) => {
    try {
      const response = await api.post(
        "/api/access-control/check",
        {
          userId: user.id,
          ownerPeerId: resource.ownerPeerId,
          resourceId: resource.resourceId,
          resourceName: resource.resourceName,
          action: action.toLowerCase(),
        },
        {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        }
      );
      return response.data;
    } catch (error) {
      if (error.response?.status === 403) {
        throw new Error("Access denied: " + error.response.data);
      }
      throw new Error("Error verifying access rights");
    }
  };

  const handleView = async (resource) => {
    if (await checkBlockStatus()) return;
    try {
      // Check access and get full resource details
      const accessResponse = await checkResourceAccess(resource, "view");

      // Merge existing resource data with access check response
      const fullResource = {
        ...resource,
        ...accessResponse,
        type: accessResponse.jpgResource ? "JPG" : "Numeric",
      };

      setSelectedResource(fullResource);
      setViewOpen(true);

      if (fullResource.type === "JPG") {
        try {
          const response = await api.get("/api/resources/image", {
            params: { path: fullResource.jpgResource },
            responseType: "blob",
            headers: {
              Authorization: createBasicAuthHeader(
                user.username,
                user.password
              ),
            },
          });

          const objectURL = URL.createObjectURL(response.data);
          setSelectedResource((prev) => ({
            ...prev,
            objectURL: objectURL,
          }));
        } catch (error) {
          console.error("Error fetching image:", error);
        }
      }
    } catch (error) {
      alert(error.message);
    }
  };

  const handleEdit = async (resource) => {
    if (await checkBlockStatus()) return;
    try {
      // First verify edit access rights and get full resource details
      const accessResponse = await checkResourceAccess(resource, "edit");

      // Merge existing metadata with detailed resource data
      const fullResource = {
        ...resource,
        ...accessResponse,
        type: accessResponse.jpgResource ? "jpg" : "numeric",
      };

      // Set up the form with retrieved resource details
      setResourceForm({
        resourceName: fullResource.resourceName,
        type: fullResource.type,
        file: null,
        dataResource: fullResource.dataResource || "",
      });

      setSelectedResource(fullResource);
      setEditOpen(true);
    } catch (error) {
      alert(error.message);
    }
  };

  const handleDelete = async (resourceId) => {
    if (await checkBlockStatus()) return;
    try {
      const resource = otherPeersResources.find(
        (r) => r.resourceId === resourceId
      );
      await checkResourceAccess(resource, "delete");

      await api.delete(`/api/resources/${resourceId}`, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });

      setOtherPeersResources((prev) =>
        prev.filter((r) => r.resourceId !== resourceId)
      );
    } catch (error) {
      alert(error.message);
    }
  };

  // Handle sending the permission request to the backend
  const handlePermissionRequest = async (resource) => {
    if (!resource.ownerPeerId) {
      alert("Object Peer ID not available. Cannot send permission request.");
      return;
    }

    const payload = {
      objectPeerId: resource.ownerPeerId, // Extract from resource (assuming ownerPeerId represents objectPeerId)
      subjectPeerId: user.id, // Current user making the request
      resourceId: resource.resourceId,
      view: permissionRequestSettings.view ? "allowed" : "disallowed",
      edit: permissionRequestSettings.edit ? "allowed" : "disallowed",
      delete: permissionRequestSettings.delete ? "allowed" : "disallowed",
    };

    try {
      await api.post("/api/access-request/request-permissions", payload, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });
      alert("Permission request successfully sent.");
      setExpandedPermissionResourceId(null);
      setPermissionRequestSettings({ view: false, edit: false, delete: false });
    } catch (error) {
      console.error("Error sending permission request", error);
      alert("Failed to send permission request.");
    }
  };

  const handleUpdate = async () => {
    try {
      setLoading(true);
      const formData = new FormData();
      formData.append("resourceName", resourceForm.resourceName);

      if (resourceForm.type === "jpg" && resourceForm.file) {
        formData.append("file", resourceForm.file);
      } else if (resourceForm.type === "numeric") {
        formData.append("dataResource", resourceForm.dataResource);
      }

      // Step 3: Upload File or Update Data
      await api.put(`/api/resources/${selectedResource.resourceId}`, formData, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
          "Content-Type": "multipart/form-data",
        },
      });

      alert("Resource access updated successfully");
      fetchOtherPeersResources();
      setEditOpen(false);
    } catch (error) {
      console.error("Error updating resource access:", error);
      alert("Failed to update resource access");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOtherPeersResources();
  }, [search, page, rowsPerPage, user]);

  return (
    <Box>
      <TextField
        fullWidth
        label="Search Resource Name"
        variant="outlined"
        value={search}
        onChange={(e) => {
          setSearch(e.target.value);
          setPage(0);
        }}
        sx={{ mb: 2 }}
      />

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Resource Name</TableCell>
              <TableCell>Owner</TableCell>
              <TableCell>Access Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {otherPeersResources.map((resource) => (
              <React.Fragment key={resource.resourceId}>
                <TableRow>
                  <TableCell>{resource.resourceId}</TableCell>
                  <TableCell>{resource.resourceName}</TableCell>
                  <TableCell>{resource.ownerPeerName}</TableCell>
                  <TableCell>
                    {resource.accessControlStatus === "Not Created" ? (
                      <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={() => handleRequestAccess(resource)}
                      >
                        Request Access
                      </Button>
                    ) : resource.accessControlStatus === "Pending" ? (
                      <Chip label="Pending" color="warning" />
                    ) : (
                      <>
                        <Chip label="Approved" color="success" />
                        <IconButton
                          onClick={() =>
                            setExpandedPermissionResourceId(
                              expandedPermissionResourceId ===
                                resource.resourceId
                                ? null
                                : resource.resourceId
                            )
                          }
                          size="small"
                        >
                          <MoreVertIcon />
                        </IconButton>
                      </>
                    )}
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      color="info"
                      sx={{ mr: 1 }}
                      onClick={() => handleView(resource)}
                      disabled={
                        !resource.actionsPermissions ||
                        (resource.actionsPermissions &&
                          resource.actionsPermissions.view !== "valid")
                      }
                    >
                      Details
                    </Button>
                    <Button
                      variant="outlined"
                      color="warning"
                      sx={{ mr: 1 }}
                      onClick={() => handleEdit(resource)}
                      disabled={
                        !resource.actionsPermissions ||
                        (resource.actionsPermissions &&
                          resource.actionsPermissions.edit !== "valid")
                      }
                    >
                      Edit
                    </Button>
                    <Button
                      variant="outlined"
                      color="error"
                      onClick={() => handleDelete(resource.resourceId)}
                      disabled={
                        !resource.actionsPermissions ||
                        (resource.actionsPermissions &&
                          resource.actionsPermissions.delete !== "valid")
                      }
                    >
                      Delete
                    </Button>
                  </TableCell>
                </TableRow>

                {expandedPermissionResourceId === resource.resourceId && (
                  <TableRow>
                    <TableCell colSpan={5} style={{ padding: 0 }}>
                      <Collapse in={true}>
                        <Box sx={{ p: 2, border: "1px solid #ccc", mt: 1 }}>
                          <Typography variant="subtitle1" sx={{ mb: 1 }}>
                            Request Permissions:
                          </Typography>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={permissionRequestSettings.view}
                                onChange={(e) =>
                                  setPermissionRequestSettings((prev) => ({
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
                                checked={permissionRequestSettings.edit}
                                onChange={(e) =>
                                  setPermissionRequestSettings((prev) => ({
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
                                checked={permissionRequestSettings.delete}
                                onChange={(e) =>
                                  setPermissionRequestSettings((prev) => ({
                                    ...prev,
                                    delete: e.target.checked,
                                  }))
                                }
                              />
                            }
                            label="Delete"
                          />
                          <Box sx={{ mt: 2 }}>
                            <Button
                              variant="contained"
                              color="primary"
                              onClick={() => handlePermissionRequest(resource)}
                              disabled={loading}
                            >
                              {loading ? (
                                <CircularProgress size={24} />
                              ) : (
                                "Submit Request"
                              )}
                            </Button>
                          </Box>
                        </Box>
                      </Collapse>
                    </TableCell>
                  </TableRow>
                )}
              </React.Fragment>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* View Dialog */}
      <Dialog open={viewOpen} onClose={() => setViewOpen(false)} maxWidth="md">
        <DialogTitle>Resource Details</DialogTitle>
        <DialogContent>
          {selectedResource && (
            <>
              <Typography variant="h6" gutterBottom>
                {selectedResource.resourceName}
              </Typography>
              <Typography variant="body1">
                Owner: {selectedResource.ownerPeerName}
              </Typography>
              {selectedResource.type === "JPG" &&
                selectedResource.objectURL && (
                  <img
                    src={selectedResource.objectURL}
                    alt="Resource"
                    style={{ maxWidth: "100%", marginTop: "1rem" }}
                  />
                )}
              {selectedResource.dataResource && (
                <Box sx={{ mt: 2 }}>
                  <Typography variant="subtitle1">Data Content:</Typography>
                  <Typography variant="body2">
                    {selectedResource.dataResource}
                  </Typography>
                </Box>
              )}
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setViewOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={blockedDialogOpen}
        onClose={() => setBlockedDialogOpen(false)}
      >
        <DialogTitle>Access Blocked</DialogTitle>
        <DialogContent>
          <Typography variant="body1">
            Your account is blocked until{" "}
            {new Date(blockEndTime).toLocaleString()}
          </Typography>
          <Typography variant="body2" sx={{ mt: 2 }}>
            Time remaining: {timeLeft}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setBlockedDialogOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      {editOpen && (
        <Dialog
          open={editOpen}
          onClose={() => setEditOpen(false)}
          maxWidth="md"
        >
          <DialogTitle>Edit Resource Access</DialogTitle>
          <DialogContent dividers>
            <TextField
              fullWidth
              label="Resource Name"
              value={resourceForm.resourceName}
              onChange={(e) =>
                setResourceForm((prev) => ({
                  ...prev,
                  resourceName: e.target.value,
                }))
              }
              margin="normal"
            />

            <RadioGroup
              row
              name="type"
              value={resourceForm.type}
              onChange={(e) =>
                setResourceForm((prev) => ({
                  ...prev,
                  type: e.target.value,
                }))
              }
            >
              <FormControlLabel value="jpg" control={<Radio />} label="JPG" />
              <FormControlLabel
                value="numeric"
                control={<Radio />}
                label="Numeric Data"
              />
            </RadioGroup>

            {resourceForm.type === "jpg" ? (
              <Button
                variant="contained"
                component="label"
                fullWidth
                sx={{ mt: 2 }}
              >
                Replace JPG File
                <input
                  type="file"
                  hidden
                  accept="image/jpeg,image/jpg"
                  onChange={(e) => {
                    setResourceForm((prev) => ({
                      ...prev,
                      file: e.target.files[0], // Store the selected file
                    }));
                  }}
                />
              </Button>
            ) : (
              <TextField
                fullWidth
                label="Numeric Data"
                margin="normal"
                name="dataResource"
                value={resourceForm.dataResource}
                onChange={(e) =>
                  setResourceForm((prev) => ({
                    ...prev,
                    dataResource: e.target.value,
                  }))
                }
              />
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setEditOpen(false)}>Cancel</Button>
            <Button
              onClick={handleUpdate}
              variant="contained"
              color="primary"
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : "Update Access"}
            </Button>
          </DialogActions>
        </Dialog>
      )}

      <TablePagination
        component="div"
        count={totalElements}
        page={page}
        onPageChange={(_, newPage) => setPage(newPage)}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={(e) => {
          setRowsPerPage(parseInt(e.target.value, 10));
          setPage(0);
        }}
      />
    </Box>
  );
};

export default OtherPeersResources;
