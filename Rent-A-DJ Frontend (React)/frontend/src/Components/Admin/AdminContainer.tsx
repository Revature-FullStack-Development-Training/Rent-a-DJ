import axios from "axios";
import { useEffect, useState } from "react";
import { Container, Button, Tabs, Tab, Form, Alert } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";
import { User } from "../UserInterface";
import { ReservationTable } from "../Reservations/ReservationTable";
import { DJTable } from "../DJs/DJTable";
import { UserTable } from "../User/UserTable";

export const AdminContainer: React.FC = () => {
  const user = useUserContext();
  const [location, setLocation] = useState("");
  const [startdatetime, setStartdatetime] = useState("");
  const [enddatetime, setEnddatetime] = useState("");
  const [djId, setDjId] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [DJs, setDjs] = useState([]);
  const [error, setError] = useState("");
  const [refresh, setRefresh] = useState(false);

  useEffect(() => {
    const fetchDjs = async () => {
      try {
        const response = await axios.get("http://localhost:7777/djs");
        setDjs(response.data);
      } catch (error) {
        console.error("Error fetching DJs:", error);
      }
    };

    fetchDjs();
  }, []);

  const createReservation = async () => {
    if (!location || !startdatetime || !enddatetime || !djId) {
      setError("All fields are required.");
      return;
    }

    const reservationData = {
      location,
      startdatetime,
      enddatetime,
      djId: Number(djId),
      userId: user.loggedID,
      status: "pending", // Hardcoded status
    };

    try {
      const response = await axios.post(
        "http://localhost:7777/reservations",
        reservationData
      );
      console.log("Reservation created:", response.data);
      setError(""); // Clear error message on successful submission
      setShowForm(false); // Hide form after successful submission
      setRefresh(!refresh); // Trigger re-render
      // Clear form fields
      setLocation("");
      setStartdatetime("");
      setEnddatetime("");
      setDjId("");
    } catch (error) {
      console.error("Error creating reservation:", error);
      setError("Error creating reservation. Please try again.");
    }
  };

  return (
    <Container>
      <h1 className="welcome-text">Welcome, {user.loggedUsername}</h1>
      <Tabs defaultActiveKey="reservations" id="admin-tabs">
        <Tab eventKey="reservations" title="Reservations" className="adminTabs">
          <ReservationTable key={refresh ? "refresh-true" : "refresh-false"}/>
          <Button className="btn-primary mb-3" onClick={() => setShowForm(!showForm)}>
        {showForm ? "Cancel" : "Create Reservation"}
      </Button>
      {showForm && (
        <Form>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form.Group controlId="formLocation">
            <Form.Label className="welcome-text">Location</Form.Label>
            <Form.Control
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              placeholder="Enter location"
            />
          </Form.Group>
          <Form.Group controlId="formStartdatetime">
            <Form.Label className="welcome-text">Start DateTime</Form.Label>
            <Form.Control
              type="datetime-local"
              value={startdatetime}
              onChange={(e) => setStartdatetime(e.target.value)}
              className="custom-datetime-input"
            />
          </Form.Group>
          <Form.Group controlId="formEnddatetime">
            <Form.Label className="welcome-text">End DateTime</Form.Label>
            <Form.Control
              type="datetime-local"
              value={enddatetime}
              onChange={(e) => setEnddatetime(e.target.value)}
              className="custom-datetime-input"
            />
          </Form.Group>
          <Form.Group controlId="formDjId">
            <Form.Label className="welcome-text">DJ</Form.Label>
            <Form.Control
              as="select"
              value={djId}
              onChange={(e) => setDjId(e.target.value)}
            >
              <option value="">Select a DJ</option>
              {DJs.map((DJ: any) => (
                <option key={DJ.djId} value={DJ.djId}>
                  {DJ.username} - ${DJ.rate}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
          
          <Button className="btn-primary mt-3" onClick={createReservation}>
            Submit Reservation
          </Button>
        </Form>
      )}
        </Tab>
        <Tab eventKey="users" title="Users" className="adminTabs">
          <UserTable />
        </Tab>
        <Tab eventKey="djs" title="DJs" className="adminTabs">
          <DJTable />
        </Tab>
      </Tabs>
    </Container>
  );
};