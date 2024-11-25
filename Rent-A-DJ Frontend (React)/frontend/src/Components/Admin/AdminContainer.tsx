import axios from "axios"
import { useEffect, useState } from "react"
import { Container, Button } from "react-bootstrap"
import { UserContext, useUserContext } from "../../context"
import { User } from "../UserInterface"
import { ReservationTable } from "../Reservations/ReservationTable"

export const AdminContainer:React.FC = () => {

    const user = useUserContext();
    

    return(

        <>
            {<p>Welcome, {user.loggedUsername}</p>}
            <ReservationTable />
        </>

    )
}