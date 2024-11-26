import axios from "axios"
import { useContext, useState } from "react"
import { Button, Container, Form } from "react-bootstrap"
import { useNavigate } from "react-router-dom"
import { UserContext, useUserContext } from "../../context"

export const Login:React.FC = () => {

    let user = useUserContext();

    const navigate = useNavigate();

    const[loginCreds, setLoginCreds] = useState({
        username:"",
        password:""
    });

    // const[loggedInUser, setloggedInUser] = useState({
    //     role:"",
    //     userID:0,
    //     username:"asdfasdfasdf"
    // });

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

                user.loggedRole = response.data.role
                user.loggedID = response.data.userId
                user.loggedUsername = response.data.username

                //saving the logged in user data globally
                //setloggedInUser(response.data)

                console.log(user)

                //greet the user
                alert("Welcome, " + user.loggedUsername)

                if(user.loggedRole === "admin"){
                    navigate("/admins")
                } else if(user.loggedRole === "user") {
                    navigate("/users")
                } else {
                    navigate("/djs")
                }

            }
        )
        .catch((error)=>{
            alert("Login Failed! Please try again.")
        })

    }

    return (
        <Container> 

            <h1>DJ Reservation</h1>
                <h3>Please Log In:</h3>
                
                <div>
                    <Form.Control
                        type="text"
                        placeholder="Username"
                        name="username"
                        onChange={storeValues}
                    />
                </div>

                <div>
                    <Form.Control
                        type="password"
                        placeholder="Password"
                        name="password"
                        onChange={storeValues}
                    />
                </div>
                

            <Button className="btn-success" onClick={login}>Login</Button>
            <Button className="btn-dark" onClick={()=>navigate("/register")}>Register</Button>
        </Container>
    )
}