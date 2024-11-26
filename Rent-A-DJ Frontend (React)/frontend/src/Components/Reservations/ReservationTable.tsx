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

    return (
        <Container>
            {user.loggedRole === 'user' &&
                <Button className="btn-warning" onClick={getPendingReservationsByUsername}>pending reservations</Button>}
            {user.loggedRole === 'user' &&
                <Button className="btn-primary" onClick={getReservationsByUsername}>all reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-warning" onClick={getAllPendingReservations}>pending reservations</Button>}
            {user.loggedRole === 'admin' &&
                <Button className="btn-primary" onClick={getAllReservations}>all reservations</Button>}

            <Table>
                <thead>
                    <tr>
                        <th>id</th>
                        <th>creation time</th>
                        <th>start date time</th>
                        <th>end date time</th>
                        <th>location</th>
                        <th>status</th>
                        <th>dj</th>
                        <th>user</th>
                        {user.loggedRole === 'admin' && <th>Actions</th>} {/* Show 'Actions' column only for admin */}
                    </tr>
                </thead>
                <tbody>
                    {reservations.map((reservation: any) => (
                        <tr key={reservation.reservationId}>
                            <td>{reservation.reservationId}</td>
                            <td>{reservation.creationTime}</td>
                            <td>{reservation.startdatetime}</td>
                            <td>{reservation.enddatetime}</td>
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
