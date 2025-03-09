import React, { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  Paper,
  Alert,
  FormControlLabel,
  Checkbox,
  Grid,
  CircularProgress,
} from "@mui/material";
import api from "../../utils/api";
import "../../styles/AuthStyles.css";

function Register() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    ipAddress: "",
    blockchainAddress: "",
    usagePurpose: "",
    group: "",
    isWebUser: false,
    privateKey: "",
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await api.post("/api/role-token/assign-role", form);
      setMessage(response.data);
    } catch (error) {
      setMessage("Error: " + (error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        minHeight: "100vh",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "background.default",
      }}
    >
      <Paper elevation={3} sx={{ p: 4, width: "100%", maxWidth: 800 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          Register
        </Typography>

        {message && (
          <Alert
            severity={message.startsWith("Error") ? "error" : "info"}
            sx={{ mb: 3 }}
          >
            {message}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Username"
                name="username"
                value={form.username}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Password"
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                required
              />
            </Grid>

            {!form.isWebUser && (
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="IP Address"
                  name="ipAddress"
                  value={form.ipAddress}
                  onChange={handleChange}
                />
              </Grid>
            )}

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Blockchain Address"
                name="blockchainAddress"
                value={form.blockchainAddress}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Usage Purpose"
                name="usagePurpose"
                value={form.usagePurpose}
                onChange={handleChange}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Group"
                name="group"
                value={form.group}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Private Key"
                name="privateKey"
                value={form.privateKey}
                onChange={handleChange}
              />
            </Grid>

            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Checkbox
                    name="isWebUser"
                    checked={form.isWebUser}
                    onChange={handleChange}
                    color="primary"
                  />
                }
                label="Is Web User"
              />
            </Grid>
          </Grid>

          <Button
            fullWidth
            variant="contained"
            size="large"
            type="submit"
            disabled={loading}
            sx={{ mt: 3 }}
          >
            {loading ? <CircularProgress size={24} /> : "Register"}
          </Button>
        </Box>
      </Paper>
    </Box>
  );
}

export default Register;
