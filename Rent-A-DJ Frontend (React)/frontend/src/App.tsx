import React from 'react';
//import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Login } from './Components/LoginRegister/Login';
import { Register } from './Components/LoginRegister/Register';

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
          </Routes>
        </BrowserRouter>
      </body>
    </div>
  );
}

export default App;
