import { createContext, useContext } from "react";
import { User } from "./Components/UserInterface";


export const UserContext = createContext<User | undefined>(undefined);

export function useUserContext() {
    const user = useContext(UserContext);

    if(user === undefined){
        throw new Error("useUserContext is undefined")
    }

    return user;
}