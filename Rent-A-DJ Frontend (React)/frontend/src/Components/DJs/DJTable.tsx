import axios from "axios";
import { useEffect, useState } from "react";
import { useUserContext } from "../../context";
import { Container, Table } from "react-bootstrap";

export const DJTable: React.FC = () => {
  const user = useUserContext();

  const [DJs, setDJs] = useState([]);

  useEffect(() => {
    getAllDJs();
  }, []);

  const getAllDJs = async () => {
    const response = await axios
      .get("http://localhost:7777/djs")
      .then((response) => {
        setDJs(response.data);
      });
  };

  return(
    <Container>

        <Table>
            <thead>
                <tr>
                    <th>id</th>
                    <th>first name</th>
                    <th>last name</th>
                    <th>rate</th>
                    <th>username</th>
                </tr>
            </thead>
            <tbody>
                {DJs.map((DJ:any)=>(
                    <tr>
                        <td>{DJ.dj_id}</td>
                        <td>{DJ.first_name}</td>
                        <td>{DJ.last_name}</td>
                        <td>{DJ.rate}</td>
                        <td>{DJ.username}</td>
                    </tr>
                ))}
            </tbody>
        </Table>


    </Container>

)
};
