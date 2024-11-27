import axios from "axios";
import { useEffect, useState } from "react";
import { Container, Button, Tabs, Tab } from "react-bootstrap";
import { UserContext, useUserContext } from "../../context";
import { User } from "../UserInterface";
import { ReservationTable } from "../Reservations/ReservationTable";
import { DJTable } from "../DJs/DJTable";
import { UserTable } from "../User/UserTable";

export const AdminContainer: React.FC = () => {
  const user = useUserContext();

  return (
    <Container>
      <h1 className="welcome-text">Welcome, {user.loggedUsername}</h1>
      <Tabs defaultActiveKey="reservations" id="admin-tabs">
        <Tab eventKey="reservations" title="Reservations" className="adminTabs">
          <ReservationTable />
        </Tab>
        <Tab eventKey="users" title="Users" className="adminTabs">
          <UserTable />
        </Tab>
        <Tab eventKey="djs" title="DJs" className="adminTabs">
          <DJTable />
        </Tab>
      </Tabs>
    </Container>
  );
};