import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
//import 'bootstrap/dist/css/bootstrap.min.css';


export const Register: React.FC = () => {

    interface FormData {
        firstName: string;
        lastName: string;
        username: string;
        password: string;
        role: string;
        rate?: number; // Optional rate field for DJs
    }

    const [formData, setFormData] = useState<FormData>({
        firstName: '',
        lastName: '',
        username: '',
        password: '',
        role: 'user',
        rate: undefined
    });

    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'rate' ? parseFloat(value) : value // Convert rate to a number
        });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            if (formData.role === 'dj') {
                const response = await axios.post<{ data: any }>('http://localhost:7777/djs', formData);
                console.log('DJ registered:', response.data);
            } else {
                const response = await axios.post<{ data: any }>('http://localhost:7777/users', formData);
                console.log('User registered:', response.data);
            }
            navigate('/users'); // Navigate to the users page after successful registration
        } catch (error) {
            console.error('Error registering user:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="my-5 mx-auto" id="regForm">
            <h2 className="text-center">Register</h2>
            <div className="form-group">
                <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required className="rounded-input" placeholder='First Name' />
            </div>
            <div className="form-group">
                <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required className="rounded-input" placeholder='Last Name' />
            </div>
            <div className="form-group">
                <input type="text" name="username" value={formData.username} onChange={handleChange} required className="rounded-input" placeholder='Username'/>
            </div>
            <div className="form-group">
                <input type="password" name="password" value={formData.password} onChange={handleChange} required className="rounded-input" placeholder='Password'/>
            </div>
            <div className="form-group">
                <select
                    id="role"
                    name="role"
                    className="form-select rounded-input"
                    value={formData.role}
                    onChange={handleChange}
                    required
                >
                    <option value="user">User</option>
                    <option value="dj">DJ</option>
                </select>
            </div>
            {formData.role === 'dj' && (
                <div className="form-group">
                    <input type="number" name="rate" value={formData.rate || ''} onChange={handleChange} required className="rounded-input" placeholder='Rate'/>
                </div>
            )}
            <div className="button-group">
                <button type="submit" className="btn btn-success">Register</button>
                <button type="button" className="btn btn-dark" onClick={() => navigate('/')}>Login</button>
            </div>
        </form>
    );
};