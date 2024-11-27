import axios from "axios"
import { useContext, useEffect, useState } from "react"
import { Container, Button } from "react-bootstrap"
import { UserContext, useUserContext } from "../../context"
import { User } from "../UserInterface"
import { useLocation } from "react-router-dom"
import { ReservationTable } from "../Reservations/ReservationTable"

export const UserContainer:React.FC = () => {

    const user = useUserContext();
    

    return(

        <>
           <p className="welcome-text">Welcome, {user.loggedUsername}</p>
           <h2 className="welcome-text">Reservation Table</h2>
            <ReservationTable />
        </>

    )
}