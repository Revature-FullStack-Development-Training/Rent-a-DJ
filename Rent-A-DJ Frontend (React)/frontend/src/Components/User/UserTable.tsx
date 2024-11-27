import axios from "axios";
import { useEffect, useState } from "react";
import { useUserContext } from "../../context";
import { Container, Table, Button } from "react-bootstrap";

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

  const deleteUser = async (username: string) => {
    try {
      await axios.delete(`http://localhost:7777/users/users/${username}`);
      setUsersForList(usersForList.filter((user: any) => user.username !== username));
    } catch (error) {
      console.error("Error deleting user:", error);
    }
  };

  console.log(usersForList)
  return(
    <Container>

        <Table className="table table-striped table-dark">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Role</th>
                    <th>Username</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                {usersForList.map((userList:any)=>(
                    <tr>
                        <td>{userList.userId}</td>
                        <td>{userList.firstName}</td>
                        <td>{userList.lastName}</td>
                        <td>{userList.role}</td>
                        <td>{userList.username}</td>
                        <td>
                          <Button
                            variant="danger"
                            onClick={() => deleteUser(userList.username)}
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
