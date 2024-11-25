import React from 'react';
//import logo from './logo.svg';
//import 'bootstrap/dist/css/bootstrap.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Login } from './Components/LoginRegister/Login';
import { Register } from './Components/LoginRegister/Register';
import { UserContainer } from './Components/User/UserContainer';
import { AdminContainer } from './Components/Admin/AdminContainer';

function App() {
  return (
    <div className="App">
      <header className="App-header">
      </header>
      <body>
        <BrowserRouter>
          <Routes>
            <Route path="" element={<Login/>}/>
            <Route path="/register" element={<Register/>}/>
            <Route path="/admins" element={<AdminContainer/>}/>
            <Route path="/users" element={<UserContainer/>}/>
          </Routes>
        </BrowserRouter>
      </body>
    </div>
  );
}

export default App;
