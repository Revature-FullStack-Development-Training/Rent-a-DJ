import { createContext, useContext } from "react";
import { User } from "./Components/UserInterface";


export const UserContext = createContext<User | undefined>({
    loggedRole:"",
    loggedID:0,
    loggedUsername:""
});

export function useUserContext() {
    const user = useContext(UserContext);

    if(user === undefined){
        throw new Error("user is undefined")
    }

    return user;
}