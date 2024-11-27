import React, { useContext } from "react";
//import logo from './logo.svg';
//import 'bootstrap/dist/css/bootstrap.css';
import "./App.css";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { Login } from "./Components/LoginRegister/Login";
import { Register } from "./Components/LoginRegister/Register";
import { UserContainer } from "./Components/User/UserContainer";
import { AdminContainer } from "./Components/Admin/AdminContainer";
import { UserContext, useUserContext } from "./context";
import { DJContainer } from "./Components/DJs/DJContainer";
import { ReservationCreator } from "./Components/Reservations/ReservationCreator";
//import 'bootstrap/dist/css/bootstrap.min.css';

function App() {

  const user = useUserContext();


  return (
    <div className="App">
      <UserContext.Provider value={user}>
        <BrowserRouter>
          <Link to="/">
            <button className="logOut">&#8962;</button>
          </Link>
          <Routes>
            <Route path="" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/admins" element={<AdminContainer />} />
            <Route path="/users" element={<UserContainer />} />
            <Route path="/djs" element={<DJContainer />} />
            <Route path="/reservations" element={<ReservationCreator />} />
          </Routes>
        </BrowserRouter>
      </UserContext.Provider>
    </div>
  );
}

export default App;
