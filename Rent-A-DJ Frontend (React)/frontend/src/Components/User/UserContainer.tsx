import axios from "axios"
import { useEffect, useState } from "react"
import { Container, Button } from "react-bootstrap"
import { UserContext } from "../../context"
import { User } from "../UserInterface"
import { useLocation } from "react-router-dom"

export const UserContainer:React.FC = () => {

    const { state } = useLocation();
    const { testID, testRole, testUsername } = state;

    //may need to fix sorry
    const[user] = useState<User>({
        loggedID:testID,
        loggedRole:testRole,
        loggedUsername:testUsername,
    });
    

    return(

        <>
            <UserContext.Provider value={user}>
                <p>hello {user.loggedID}</p>
            </UserContext.Provider>
        </>

    )
}