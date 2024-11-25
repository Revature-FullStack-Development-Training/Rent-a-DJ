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
            {<p>Welcome, {user.loggedUsername}</p>}
            <ReservationTable />
        </>

    )
}