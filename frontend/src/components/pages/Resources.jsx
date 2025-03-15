import React, { useState, useEffect } from "react";
import {
  Box,
  Tabs,
  Tab,
  TextField,
  Radio,
  RadioGroup,
  FormControlLabel,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
  TablePagination,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  styled,
} from "@mui/material";
import api, { createBasicAuthHeader } from "../../utils/api";

const ResourceForm = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(3),
  marginTop: theme.spacing(2),
}));

function Resources({ user }) {
  // Active tab: "add" for adding a new resource vs "list" for displaying resources.
  const [activeTab, setActiveTab] = useState("add");

  // Form state for adding/updating a resource.
  const [resourceForm, setResourceForm] = useState({
    resourceName: "",
    type: "jpg", // "jpg" or "numeric"
    file: null,
    dataResource: "",
  });

  const [loading, setLoading] = useState(false);

  // State for the resource list.
  const [resources, setResources] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [totalElements, setTotalElements] = useState(0);

  // Modal states for View and Edit dialogs.
  const [viewOpen, setViewOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [selectedResource, setSelectedResource] = useState(null);

  // Handle input changes.
  const handleChange = (e) => {
    const { name, value } = e.target;
    setResourceForm((prev) => ({ ...prev, [name]: value }));
  };

  // Handle file input (for JPG uploads).
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setResourceForm((prev) => ({ ...prev, file }));
  };

  // Submit a new resource using multipart form data.
  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    // Send the username as a parameter so that the backend can resolve the Peer.
    formData.append("username", user.username);
    formData.append("resourceName", resourceForm.resourceName);

    if (resourceForm.type === "jpg" && resourceForm.file) {
      formData.append("file", resourceForm.file);
    } else if (resourceForm.type === "numeric") {
      formData.append("dataResource", resourceForm.dataResource);
    }

    try {
      setLoading(true);
      await api.post(`/api/resources`, formData, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Resource created");
      // Clear form and refresh the list.
      setResourceForm({
        resourceName: "",
        type: "jpg",
        file: null,
        dataResource: "",
      });
      fetchResources();
    } catch (error) {
      console.error("Error creating resource", error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch resources with search and pagination.
  const fetchResources = async () => {
    try {
      const res = await api.get("/api/resources", {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
        params: {
          username: user.username,
          search: search,
          page: page,
          size: rowsPerPage,
        },
      });
      const data = res.data;
      const content = data.content || data;
      // Determine resource type: If dataResource exists then "Numeric", otherwise "JPG".
      const formattedResources = content.map((resource) => ({
        ...resource,
        type: resource.dataResource ? "Numeric" : "JPG",
      }));
      setResources(formattedResources);
      setTotalElements(
        data.totalElements ||
          (data.content ? data.totalElements : content.length)
      );
    } catch (error) {
      console.error("Error fetching resources", error);
    }
  };

  // When the "list" tab is active (or when search/pagination changes), fetch resources.
  useEffect(() => {
    if (activeTab === "list") {
      fetchResources();
    }
  }, [activeTab, search, page, rowsPerPage]);

  const handleView = async (resource) => {
    setSelectedResource(resource);
    setViewOpen(true);

    if (resource.type === "JPG") {
      try {
        const response = await fetch(
          `http://localhost:8080/api/resources/image?path=${encodeURIComponent(
            resource.jpgResource
          )}`,
          {
            headers: {
              Authorization: createBasicAuthHeader(
                user.username,
                user.password
              ),
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch image");
        }

        const blob = await response.blob();
        const objectURL = URL.createObjectURL(blob);
        setSelectedResource((prev) => ({ ...prev, objectURL }));
      } catch (error) {
        console.error("Error fetching image:", error);
      }
    } else {
      try {
        const response = await fetch(
            `http://localhost:8080/api/resources/numeric-data?id=${resource.id}`,
            {
                headers: {
                    Authorization: createBasicAuthHeader(user.username, user.password),
                },
            }
        );

        if (!response.ok) {
            throw new Error("Failed to fetch numeric data");
        }

        const data = await response.text();
        setSelectedResource((prev) => ({ ...prev, dataResource: data }));
    } catch (error) {
        console.error("Error fetching numeric data:", error);
    }
    }
  };

  // Open the edit dialog with the selected resource.
  const handleEdit = (resource) => {
    setSelectedResource(resource);
    setResourceForm({
      resourceName: resource.resourceName,
      type: resource.type.toLowerCase(),
      file: null,
      dataResource: resource.dataResource || "",
    });
    setEditOpen(true);
  };

  // Update a resource.
  const handleUpdate = async () => {
    const formData = new FormData();
    formData.append("resourceName", resourceForm.resourceName);
    if (resourceForm.type === "jpg" && resourceForm.file) {
      formData.append("file", resourceForm.file);
    } else if (resourceForm.type === "numeric") {
      formData.append("dataResource", resourceForm.dataResource);
    }
    try {
      setLoading(true);
      await api.put(`/api/resources/${selectedResource.id}`, formData, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Resource updated");
      fetchResources();
      setEditOpen(false);
    } catch (error) {
      console.error("Error updating resource", error);
    } finally {
      setLoading(false);
    }
  };

  // Delete a resource.
  const handleDelete = async (id) => {
    try {
      await api.delete(`/api/resources/${id}`, {
        headers: {
          Authorization: createBasicAuthHeader(user.username, user.password),
        },
      });
      fetchResources();
    } catch (error) {
      console.error("Error deleting resource", error);
    }
  };

  // Pagination handlers.
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Resources Management
      </Typography>

      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs value={activeTab} onChange={(e, newVal) => setActiveTab(newVal)}>
          <Tab label="Add Resource" value="add" />
          <Tab label="Resource List" value="list" />
        </Tabs>
      </Box>

      {activeTab === "add" ? (
        <ResourceForm elevation={3}>
          <TextField
            fullWidth
            label="Resource Name"
            margin="normal"
            name="resourceName"
            value={resourceForm.resourceName}
            onChange={handleChange}
          />

          <RadioGroup
            row
            name="type"
            value={resourceForm.type}
            onChange={handleChange}
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
              Upload JPG
              <input
                type="file"
                hidden
                accept="image/jpeg,image/jpg"
                onChange={handleFileChange}
              />
            </Button>
          ) : (
            <TextField
              fullWidth
              label="Numeric Data (comma separated)"
              margin="normal"
              name="dataResource"
              value={resourceForm.dataResource}
              onChange={handleChange}
            />
          )}

          <Button
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3 }}
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} /> : "Submit Resource"}
          </Button>
        </ResourceForm>
      ) : (
        <Box sx={{ mt: 2 }}>
          {/* Search field */}
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
                  <TableCell>Type</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {resources.map((resource) => (
                  <TableRow key={resource.id}>
                    <TableCell>{resource.id}</TableCell>
                    <TableCell>{resource.resourceName}</TableCell>
                    <TableCell>{resource.type}</TableCell>
                    <TableCell>
                      <Button
                        variant="outlined"
                        color="info"
                        onClick={() => handleView(resource)}
                      >
                        View
                      </Button>
                      <Button
                        variant="outlined"
                        color="warning"
                        onClick={() => handleEdit(resource)}
                        sx={{ mx: 1 }}
                      >
                        Edit
                      </Button>
                      <Button
                        variant="outlined"
                        color="error"
                        onClick={() => handleDelete(resource.id)}
                      >
                        Delete
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <TablePagination
            component="div"
            count={totalElements}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </Box>
      )}

      {/* View Resource Dialog */}
      <Dialog open={viewOpen} onClose={() => setViewOpen(false)}>
        <DialogTitle>Resource Preview</DialogTitle>
        <DialogContent>
          {selectedResource && selectedResource.type === "JPG" ? (
            <img
              src={selectedResource.objectURL || ""}
              alt="Resource"
              width="400"
            />
          ) : (
            <Typography variant="body1">
              {selectedResource?.dataResource || "No data available"}
            </Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setViewOpen(false)} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit Resource Dialog */}
      <Dialog
        open={editOpen}
        onClose={() => setEditOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Edit Resource</DialogTitle>
        <DialogContent dividers sx={{ minWidth: "500px", padding: 3 }}>
          <TextField
            fullWidth
            label="Resource Name"
            name="resourceName"
            value={resourceForm.resourceName}
            onChange={handleChange}
            margin="dense"
          />

          <RadioGroup
            row
            name="type"
            value={resourceForm.type}
            onChange={handleChange}
            sx={{ my: 2 }}
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
              sx={{ mt: 2, p: 1.5 }}
            >
              Upload New JPG
              <input
                type="file"
                hidden
                accept="image/jpeg,image/jpg"
                onChange={handleFileChange}
              />
            </Button>
          ) : (
            <TextField
              fullWidth
              label="Numeric Data"
              margin="normal"
              name="dataResource"
              value={resourceForm.dataResource}
              onChange={handleChange}
            />
          )}
        </DialogContent>

        <DialogActions>
          <Button onClick={handleUpdate} color="primary" variant="contained">
            Update
          </Button>
          <Button onClick={() => setEditOpen(false)} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Resources;
