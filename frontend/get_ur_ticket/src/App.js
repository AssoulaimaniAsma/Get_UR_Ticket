import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/User/Home';
import EventDetails from './pages/EventDetails';
import MyReservations from './pages/MyReservations';
import Profile from './pages/Profile';
import CreateEvent from './pages/Organizer/CreateEvent';
import MyEvents from './pages/Organizer/MyEvents'
import ManageEvents from './pages/Admin/ManageEvents'
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
                    <Route
                        path="/organizer/create-event"
                        element={
                            <PrivateRoute>
                                <CreateEvent />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/admin/manage-events"
                        element={
                            <PrivateRoute>
                                <ManageEvents />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/organizer/my-events"
                        element={
                            <PrivateRoute>
                                <MyEvents />
                            </PrivateRoute>
                        }
                    />

                </Routes>
            </div>
        </Router>
    );
}

export default App;