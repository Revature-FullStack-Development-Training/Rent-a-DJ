import axios from "axios";
import { useContext, useEffect, useState } from "react"
import { Button, Container, Table } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";

export const ReservationTable: React.FC = () => {
    const user = useUserContext();
    const [reservations, setReservations] = useState<any[]>([]);

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
        const response = await axios.get("http://localhost:7777/reservations")
            .then((response) => {
                setReservations(response.data);
            });
    };

    const getReservationsByDJ = async () => {
        const response = await axios.get("http://localhost:7777/reservations/dj/" + user.loggedID + "/username/" + user.loggedUsername)
            .then((response) => {
                setReservations(response.data);
            });
    };

    const getReservationsByUsername = async () => {
        const response = await axios.get("http://localhost:7777/reservations/user/" + user.loggedUsername)
            .then((response) => {
                setReservations(response.data);
            });
    };

    const getPendingReservationsByUsername = async () => {
        const response = await axios.get("http://localhost:7777/reservations/user/" + user.loggedUsername + "/pending")
            .then((response) => {
                setReservations(response.data);
            });
    };

    const getAllPendingReservations = async () => {
        const response = await axios.get("http://localhost:7777/reservations/pending")
            .then((response) => {
                setReservations(response.data);
            });
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

    return (
        <Container>
            {user.loggedRole === 'user' &&
                <Button onClick={getPendingReservationsByUsername}>pending reservations</Button>}
            {user.loggedRole === 'user' &&
                <Button className="btn-primary" onClick={getReservationsByUsername}>all reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-warning" onClick={getAllPendingReservations}>pending reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-primary" onClick={getAllReservations}>all reservations</Button>}

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
                            <td>{reservation.location}</td>
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
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </Table>
        </Container>
    );
};
