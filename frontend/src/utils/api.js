import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // Set the base URL
  headers: {
    "Content-Type": "application/json",
  },
});

// Function to generate Basic Auth header
export const createBasicAuthHeader = (username, password) => {
  return "Basic " + btoa(`${username}:${password}`);
};

export default api;
