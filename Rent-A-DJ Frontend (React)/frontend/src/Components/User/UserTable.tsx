import axios from "axios";
import { useEffect, useState } from "react";
import { useUserContext } from "../../context";
import { Container, Table } from "react-bootstrap";

export const UserTable: React.FC = () => {
  const user = useUserContext();

  const [usersForList, setUsersForList] = useState([]);

  useEffect(() => {
    getAllUsersForList();
  }, []);

  const getAllUsersForList = async () => {
    const response = await axios
      .get("http://localhost:7777/users")
      .then((response) => {
        setUsersForList(response.data);
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
                    <th>role</th>
                    <th>username</th>
                </tr>
            </thead>
            <tbody>
                {usersForList.map((userList:any)=>(
                    <tr>
                        <td>{userList.user_id}</td>
                        <td>{userList.first_name}</td>
                        <td>{userList.last_name}</td>
                        <td>{userList.role}</td>
                        <td>{userList.username}</td>
                    </tr>
                ))}
            </tbody>
        </Table>


    </Container>

)
};
