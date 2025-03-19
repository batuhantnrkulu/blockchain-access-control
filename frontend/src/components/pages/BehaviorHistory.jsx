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
  TableSortLabel,
} from "@mui/material";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  ChartDataLabels
);

const ChartContainer = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(3),
  marginBottom: theme.spacing(3),
  height: 400,
}));

function BehaviorHistory({ user }) {
  const theme = useTheme();
  const [history, setHistory] = useState([]);
  const [selectedRow, setSelectedRow] = useState(null);
  const [sortOrder, setSortOrder] = useState("desc");

  useEffect(() => {
    // Commenting API call for now
    // async function fetchHistory() {
    //   try {
    //     const res = await api.get(`/api/behavior-history/${user.username}`, {
    //       headers: {
    //         Authorization: createBasicAuthHeader(user.username, user.password),
    //       },
    //     });
    //     setHistory(res.data);
    //   } catch (error) {
    //     console.error("Error fetching behavior history", error);
    //   }
    // }
    // fetchHistory();
    
    // Dummy data for testing
    const dummyData = Array.from({ length: 20 }, (_, i) => ({
      id: i + 1,
      statusUpdateFormatted: new Date(Date.now() - i * 60000).toISOString(),
      tokenAmount: Math.floor(Math.random() * 200) - 100,
      reason: i % 2 === 0 ? "Positive behavior" : "Negative behavior",
    }));
    setHistory(dummyData);
  }, []);

  const sortedHistory = [...history].sort((a, b) => {
    if (sortOrder === "asc") {
      return new Date(a.statusUpdateFormatted) - new Date(b.statusUpdateFormatted);
    } else {
      return new Date(b.statusUpdateFormatted) - new Date(a.statusUpdateFormatted);
    }
  });

  // Calculate token differences
  const tokenDifferences = sortedHistory.map((entry, index) => {
    if (index === 0) return { ...entry, difference: 0 };
    const previousEntry = sortedHistory[index - 1];
    return { ...entry, difference: entry.tokenAmount - previousEntry.tokenAmount };
  });

  const chartData = {
    labels: sortedHistory.map((item) =>
      new Date(item.statusUpdateFormatted).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      })
    ),
    datasets: [
      {
        label: "Token Amount",
        data: sortedHistory.map((item) => item.tokenAmount),
        borderColor: "rgba(75,192,192,1)",
        backgroundColor: "rgba(75,192,192,0.2)",
        fill: true,
        tension: 0.3,
        pointBackgroundColor: sortedHistory.map((item) =>
          selectedRow === item.id ? "red" : "rgba(75,192,192,1)"
        ),
        pointBorderColor: "#fff",
        pointHoverRadius: 5,
        pointRadius: 3,
        datalabels: {
          align: "top",
          color: "#333",
          font: { size: 12 },
          formatter: (value, context) => {
            const index = context.dataIndex;
            return sortedHistory[index]?.reason || "";
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

  const handleSortRequest = () => {
    setSortOrder((prevOrder) => (prevOrder === "asc" ? "desc" : "asc"));
  };

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Behavior History
      </Typography>

      <ChartContainer elevation={3}>
        <Line data={chartData} options={chartOptions} />
      </ChartContainer>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sortDirection={sortOrder}>
                <TableSortLabel
                  active
                  direction={sortOrder}
                  onClick={handleSortRequest}
                >
                  Timestamp
                </TableSortLabel>
              </TableCell>
              <TableCell align="right">Token Amount</TableCell>
              <TableCell align="right">Difference</TableCell>
              <TableCell>Reason</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tokenDifferences.map((entry) => (
              <TableRow
                key={entry.id}
                onClick={() => setSelectedRow(entry.id)}
                style={{
                  backgroundColor: selectedRow === entry.id ? "#f0f8ff" : "inherit",
                  cursor: "pointer",
                }}
              >
                <TableCell>
                  {new Date(entry.statusUpdateFormatted).toLocaleString()}
                </TableCell>
                <TableCell align="right">{entry.tokenAmount}</TableCell>
                <TableCell align="right">{entry.difference}</TableCell>
                <TableCell>{entry.reason}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

export default BehaviorHistory;
