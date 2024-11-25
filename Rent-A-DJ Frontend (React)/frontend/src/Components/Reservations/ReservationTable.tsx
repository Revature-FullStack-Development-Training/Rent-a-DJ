import axios from "axios";
import { useContext, useEffect, useState } from "react"
import { Button, Container, Table } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";

export const ReservationTable:React.FC = () => {
    const user = useUserContext();

    const[reservations, setReservations] = useState([])

    useEffect(()=>{
        if(user.loggedRole === "admin")
        {
            getAllReservations()
        } else {
            getReservationsByUsername()
        }
    }, [])

    const getAllReservations = async () => {
        const response = await axios.get("http://localhost:7777/reservations")
        .then(
            (response) => {
                setReservations(response.data)
            }
        )
    }

    const getReservationsByUsername = async () => {
        const response = await axios.get("http://localhost:7777/reservations/user/" + user.loggedUsername)
        .then(
            (response) => {
                setReservations(response.data)
            }
        )
    }

    return(
        <Container>

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
                    </tr>
                </thead>
                <tbody>
                    {reservations.map((reservation:any)=>(
                        <tr>
                            <td>{reservation.reservationId}</td>
                            <td>{reservation.creationTime}</td>
                            <td>{reservation.startdatetime}</td>
                            <td>{reservation.enddatetime}</td>
                            <td>{reservation.location}</td>
                            <td>{reservation.status}</td>
                            <td>{reservation.dj.djId}</td>
                            <td>{reservation.user.userId}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>


        </Container>

    )
}