import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { Button, Container, Form, Table } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";

export const ReservationTable: React.FC = () => {
    const user = useUserContext();
    const [reservations, setReservations] = useState<any[]>([]);
    const [location, setLocation] = useState("");
    const [startdatetime, setStartdatetime] = useState("");
    const [enddatetime, setEnddatetime] = useState("");
    const [updating, setUpdating] = useState<number | null>(null);
    const [onlyPending, setOnlyPending] = useState<boolean>();

    useEffect(() => {
        if (user.loggedRole === "admin") {
            getAllReservations();
        } else if (user.loggedRole === "user") {
            getReservationsByUsername();
        } else if (user.loggedRole === "dj") {
            getReservationsByDJ();
        }
    }, []);

    const getAllReservations = async () => {
        setOnlyPending(false);
        const response = await axios.get("http://localhost:7777/reservations");
        setReservations(response.data);
    };

    const getReservationsByDJ = async () => {
        setOnlyPending(false);
        const response = await axios.get(`http://localhost:7777/reservations/dj/${user.loggedID}/username/${user.loggedUsername}`);
        setReservations(response.data);
    };

    const getReservationsByUsername = async () => {
        setOnlyPending(false);
        const response = await axios.get(`http://localhost:7777/reservations/user/${user.loggedUsername}`);
        setReservations(response.data);
    };

    const getPendingReservationsByUsername = async () => {
        setOnlyPending(true);
        const response = await axios.get(`http://localhost:7777/reservations/user/${user.loggedUsername}/pending`);
        setReservations(response.data);
    };

    const getAllPendingReservations = async () => {
        setOnlyPending(true);
        const response = await axios.get("http://localhost:7777/reservations/pending");
        setReservations(response.data);
    };

    const deleteReservation = async (reservationId: number) => {
        try {
            await axios.delete(`http://localhost:7777/reservations/${reservationId}`);
            setReservations(reservations.filter(reservation => reservation.reservationId !== reservationId));
        } catch (error) {
            console.error("Error deleting reservation:", error);
        }
    };

    const formatDateTime = (dateTime: string) => {
        const date = new Date(dateTime);
        return date.toLocaleString();
    };

    const updateReservation = (reservationId: number) => {
        setUpdating(reservationId);
    };

    const saveUpdatedLocation = async (reservationId: number) => {
        const updatedLocation = { location };

        try {
            await axios.patch(
                `http://localhost:7777/reservations/${reservationId}/location`,
                location, // Send the raw string
                {
                    headers: {
                        "Content-Type": "text/plain", // Specify the content type if required
                    },
                }
            );
            setUpdating(null); // Hide form after successful submission
            // Fetch updated table data
            if (onlyPending) {
                await getAllPendingReservations();
            } else {
                await getAllReservations();
            }
        } catch (error) {
            console.error("Error updating reservation:", error);
        }
    };

    const cancelUpdate = () => {
        setUpdating(null); // Exit edit mode without saving
    };

    return (
        <Container>
            {user.loggedRole === 'user' &&
                <Button className="btn-warning reservButton" onClick={getPendingReservationsByUsername}>Pending Reservations</Button>}
            {user.loggedRole === 'user' &&
                <Button className="btn-primary reservButton" onClick={getReservationsByUsername}>All Reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-warning reservButton" onClick={getAllPendingReservations}>Pending Reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-primary reservButton" onClick={getAllReservations}>All Reservations</Button>}

            <h2 className="welcome-text">Reservations</h2>
            <Table className="table table-dark table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Creation Time</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Location</th>
                        <th>Status</th>
                        <th>DJ</th>
                        <th>User</th>
                        {user.loggedRole === 'admin' && <th>Actions</th>} {/* Show 'Actions' column only for admin */}
                    </tr>
                </thead>
                <tbody>
                    {reservations.map((reservation: any) => (
                        <tr key={reservation.reservationId}>
                            <td>{reservation.reservationId}</td>
                            <td>{formatDateTime(reservation.creationTime)}</td>
                            <td>{formatDateTime(reservation.startdatetime)}</td>
                            <td>{formatDateTime(reservation.enddatetime)}</td>
                            <td>
                                {updating === reservation.reservationId ? 
                                <>
                                    <Form.Control
                                        type="text"
                                        value={location}
                                        onChange={(e) => setLocation(e.target.value)}
                                    /> 
                                    <Button
                                        variant="success"
                                        size="sm"
                                        onClick={() => saveUpdatedLocation(reservation.reservationId)}
                                    >
                                        Save
                                    </Button>
                                    <Button variant="secondary" size="sm" onClick={cancelUpdate}>
                                        Cancel
                                    </Button>
                                </>
                                : (<>{reservation.location}</>)}
                            </td>
                            <td>{reservation.status}</td>
                            <td>{reservation.dj.djId}</td>
                            <td>{reservation.user.userId}</td>
                            {user.loggedRole === 'admin' && (
                                <td>
                                    <Button
                                        variant="danger"
                                        onClick={() => deleteReservation(reservation.reservationId)}
                                    >
                                        Delete
                                    </Button>
                                    <Button variant="primary" onClick={() => updateReservation(reservation.reservationId)}>
                                        Update
                                    </Button>
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </Table>
        </Container>
    );
};
