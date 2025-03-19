import React, { useState, useEffect } from "react";
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  useTheme,
  styled,
} from "@mui/material";
import { Line } from "react-chartjs-2";
import api, { createBasicAuthHeader } from "../../utils/api";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels"; // Import the plugin

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  ChartDataLabels // Register the data labels plugin
);

const ChartContainer = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(3),
  marginTop: theme.spacing(3),
  height: 400,
}));

function BehaviorHistory({ user }) {
  const theme = useTheme();
  const [history, setHistory] = useState([]);

  useEffect(() => {
    async function fetchHistory() {
      try {
        const res = await api.get(`/api/behavior-history/${user.username}`, {
          headers: {
            Authorization: createBasicAuthHeader(user.username, user.password),
          },
        });
        setHistory(res.data);
      } catch (error) {
        console.error("Error fetching behavior history", error);
      }
    }
    fetchHistory();
  }, [user]);

  const reversedHistory = history.slice().reverse(); // reverse the data because it retrieves in desc order.

  const chartData = {
    labels: reversedHistory.map((item) =>
      new Date(item.statusUpdateFormatted).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      })
    ),
    datasets: [
      {
        label: "Token Amount",
        data: reversedHistory.map((item) => item.tokenAmount),
        borderColor: "rgba(75,192,192,1)",
        backgroundColor: "rgba(75,192,192,0.2)",
        fill: true,
        tension: 0.3,
        pointBackgroundColor: "rgba(75,192,192,1)",
        pointBorderColor: "#fff",
        pointHoverRadius: 5,
        pointRadius: 3,
        datalabels: {
          align: "top",
          color: "#333",
          font: { size: 12 },
          formatter: (value, context) => {
            const index = context.dataIndex;
            return reversedHistory[index]?.reason || ""; // Show reason above each point
          },
        },
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: { color: theme.palette.text.primary },
      },
      datalabels: {
        display: true,
      },
    },
    scales: {
      x: {
        grid: { color: theme.palette.divider },
        ticks: { color: theme.palette.text.secondary },
      },
      y: {
        grid: { color: theme.palette.divider },
        ticks: { color: theme.palette.text.secondary },
      },
    },
  };

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Behavior History
      </Typography>

      {/* Table */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Timestamp</TableCell>
              <TableCell align="right">Token Amount</TableCell>
              <TableCell>Reason</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((entry) => (
              <TableRow key={entry.id}>
                <TableCell>
                  {new Date(entry.statusUpdateFormatted).toLocaleString()}
                </TableCell>
                <TableCell align="right">{entry.tokenAmount}</TableCell>
                <TableCell>{entry.reason}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Chart */}
      <ChartContainer elevation={3}>
        <Line data={chartData} options={chartOptions} />
      </ChartContainer>
    </Box>
  );
}

export default BehaviorHistory;
