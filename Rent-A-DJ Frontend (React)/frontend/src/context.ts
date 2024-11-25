import { createContext } from "react";
import { User } from "./Components/UserInterface";


export const UserContext = createContext<User | undefined>(undefined);