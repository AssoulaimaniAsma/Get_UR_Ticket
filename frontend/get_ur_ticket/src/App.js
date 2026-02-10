import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import EventDetails from './pages/EventDetails';
import MyReservations from './pages/MyReservations';
import Profile from './pages/Profile';
import { getCurrentUser } from './services/api';
import './App.css';

function PrivateRoute({ children }) {
    const user = getCurrentUser();
    return user ? children : <Navigate to="/login" />;
}

function App() {
    return (
        <Router>
            <div className="App">
                <Navbar />
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/" element={<Home />} />
                    <Route path="/event/:id" element={<EventDetails />} />
                    <Route
                        path="/my-reservations"
                        element={
                            <PrivateRoute>
                                <MyReservations />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/profile"
                        element={
                            <PrivateRoute>
                                <Profile />
                            </PrivateRoute>
                        }
                    />
                </Routes>
            </div>
        </Router>
    );
}

export default App;