import axios from "axios"
import { useState } from "react"
import { Button, Container, Form } from "react-bootstrap"
import { useNavigate } from "react-router-dom"

export const Login:React.FC = () => {

    const navigate = useNavigate();

    const[loginCreds, setLoginCreds] = useState({
        username:"",
        password:""
    });

    const[loggedInUser, setloggedInUser] = useState({
        loggedID:0,
        loggedRole:"",
        loggedUsername:""
    });

    const storeValues = (input:any) => {

        const name = input.target.name 
        const value = input.target.value

        setLoginCreds((loginCreds) => ({...loginCreds, [name]:value}))

    }

    const login = async () => {

        //use the username/password in the loginCreds state object
        const response = await axios.post("http://localhost:7777/auth", loginCreds)
        .then(
            (response) => {

                console.log(response.data)

                //saving the logged in user data globally
                setloggedInUser(response.data)

                //greet the user
                //alert("Welcome, " + loggedInUser.loggedUsername)

                if(loggedInUser.loggedRole === "admin"){
                    navigate("/admins", {state: {loggedInUser}})
                } else {
                    navigate("/users", {state: {loggedInUser}})
                }

            }
        )
        .catch((error)=>{
            alert("Login Failed! Please try again.")
        })

    }

    return (
        <Container> 

            <h1>dj reservation</h1>
                <h3>log in:</h3>
                
                <div>
                    <Form.Control
                        type="text"
                        placeholder="username"
                        name="username"
                        onChange={storeValues}
                    />
                </div>

                <div>
                    <Form.Control
                        type="password"
                        placeholder="password"
                        name="password"
                        onChange={storeValues}
                    />
                </div>
                

            <Button className="btn-success" onClick={login}>login</Button>
            <Button className="btn-dark" onClick={()=>navigate("/register")}>register</Button>
        </Container>
    )
}