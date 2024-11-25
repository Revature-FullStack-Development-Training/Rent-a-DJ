import axios from "axios"
import { useEffect, useState } from "react"
import { Container, Button } from "react-bootstrap"
import { UserContext } from "../../context"
import { User } from "../UserInterface"

export const DJContainer:React.FC = () => {

    //default values? may need to fix sorry
    const[user] = useState<User>({
        loggedID:0,
        loggedRole:"",
        loggedUsername:"",
    });
    

    return(

        <>
            <UserContext.Provider value={user}>

            </UserContext.Provider>
        </>

    )
}