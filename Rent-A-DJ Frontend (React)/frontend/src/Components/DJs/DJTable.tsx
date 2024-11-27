import axios from "axios";
import { useEffect, useState } from "react";
import { useUserContext } from "../../context";
import { Container, Table, Button } from "react-bootstrap";

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

  const deleteDJ = async (username: string) => {
    try {
      await axios.delete(`http://localhost:7777/djs/${username}`);
      setDJs(DJs.filter((DJ: any) => DJ.username !== username));
    } catch (error) {
      console.error("Error deleting DJ:", error);
    }
  };


  return(
    <Container>

        <Table className="table table-dark table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Rate</th>
                    <th>Username</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                {DJs.map((DJ:any)=>(
                    <tr>
                        <td>{DJ.djId}</td>
                        <td>{DJ.firstName}</td>
                        <td>{DJ.lastName}</td>
                        <td>{DJ.rate}</td>
                        <td>{DJ.username}</td>
                        <td>
                          <Button
                            variant="danger"
                            onClick={() => deleteDJ(DJ.username)}
                          >
                            Delete
                          </Button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>


    </Container>

)
};
