import axios from "axios";
import { useState } from "react";
import { Container, Button, Form, Alert } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";
import { ReservationTable } from "../Reservations/ReservationTable";

export const DJContainer: React.FC = () => {
  const user = useUserContext();
  const [newRate, setNewRate] = useState("");
  const [error, setError] = useState("");

  const storeValues = (input: any) => {
    const value = input.target.value;
    setNewRate(value);
  };

  const handleSubmit = async () => {
    if (!newRate || isNaN(Number(newRate)) || Number(newRate) <= 0) {
      setError("Please enter a valid number greater than 0 for the rate.");
      return;
    }

    setError(""); // Clear any previous errors

    let prejson = '{"rate": ' + newRate + '}';
    let passObject = JSON.parse(prejson);
    try {
      const response = await axios.patch(
        "http://localhost:7777/djs/" + user.loggedID + "/rate",
        passObject
      );
      console.log(response.data);
    } catch (error) {
      console.error("Error updating rate:", error);
    }
  };

  return (
    <Container>
        <h1 className="welcome-text">Welcome, {user.loggedUsername}</h1>
        <Form className="updateRate">
            <Form.Group controlId="formRate" className="form-group-inline">
                <Form.Control
                type="number"
                name="rate"
                value={newRate}
                onChange={storeValues}
                placeholder="Enter new rate"
                className="custom-input-width"
                />
                <Button className="btn-warning" onClick={handleSubmit}>
                Update Rate
                </Button>
            </Form.Group>
            {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
        </Form>
      <ReservationTable />
    </Container>
  );
};