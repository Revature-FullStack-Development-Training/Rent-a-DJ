import axios from "axios"
import { useEffect, useState } from "react"
import { Container, Button, Form } from "react-bootstrap"
import { UserContext, useUserContext } from "../../context"
import { User } from "../UserInterface"
import { ReservationTable } from "../Reservations/ReservationTable"

export const DJContainer:React.FC = () => {

    const user = useUserContext();

    const[newRate, setNewRate] = useState()

    const storeValues = (input:any) => {

        console.log(input)

        const name = "rate"
        const value = input.target.value

        setNewRate(value)
    }

    const handleSubmit = async () => {
        let prejson = '{"rate": '+ newRate + '}';
        let passObject = JSON.parse(prejson);
        const response = await axios
          .patch("http://localhost:7777/djs/" + user.loggedID + "/rate", passObject)
          .then((response) => {
            console.log(response.data);
          });
      };
    
    return(

        <>
            {<p>Welcome, {user.loggedUsername}</p>}
            <Button className="btn-warning" onClick={handleSubmit}>update rate</Button>
            <Form.Control
                        type="number"
                        name="rate"
                        onChange={storeValues}
                    />
            <ReservationTable />
        </>

    )
}