import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// Components
import Navbar from './components/Navbar';

// Auth Pages
import Login from './pages/Login';
import Register from './pages/Register';

// User Pages
import Home from './pages/User/Home';
import EventDetails from './pages/EventDetails';
import MyReservations from './pages/User/MyReservations';
import Profile from './pages/Profile';

// Organizer Pages
import CreateEvent from './pages/Organizer/CreateEvent';
import MyEvents from './pages/Organizer/MyEvents';
import EditEvent from './pages/Organizer/EditEvent';
import OrganizerReservations from './pages/Organizer/OrganizerReservations';

// Admin Pages
import ManageEvents from './pages/Admin/ManageEvents';

// Services
import { getCurrentUser } from './services/api';

// Styles
import './App.css';

// ========== COMPOSANTS DE PROTECTION ==========

// ✅ Route privée - Utilisateur connecté uniquement
const PrivateRoute = ({ children }) => {
    const user = getCurrentUser();
    return user ? children : <Navigate to="/login" />;
};

// ✅ Route protégée - Rôle spécifique requis
const ProtectedRoute = ({ children, allowedRoles }) => {
    const user = getCurrentUser();

    if (!user) {
        return <Navigate to="/login" />;
    }

    if (allowedRoles && !allowedRoles.includes(user.role)) {
        return <Navigate to="/" />;
    }

    return children;
};

// ========== APP ==========

function App() {
    return (
        <Router>
            <div className="App">
                <Navbar />
                <Routes>

                    {/* ===== ROUTES PUBLIQUES ===== */}
                    <Route path="/"            element={<Home />} />
                    <Route path="/login"       element={<Login />} />
                    <Route path="/register"    element={<Register />} />
                    <Route path="/events/:id"  element={<EventDetails />} />

                    {/* ===== ROUTES PRIVÉES (tous les rôles connectés) ===== */}
                    <Route
                        path="/profile"
                        element={
                            <PrivateRoute>
                                <Profile />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/my-reservations"
                        element={
                            <PrivateRoute>
                                <MyReservations />
                            </PrivateRoute>
                        }
                    />

                    {/* ===== ROUTES ORGANIZER ===== */}
                    <Route
                        path="/organizer/my-events"
                        element={
                            <ProtectedRoute allowedRoles={['ORGANIZER']}>
                                <MyEvents />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/organizer/create-event"
                        element={
                            <ProtectedRoute allowedRoles={['ORGANIZER']}>
                                <CreateEvent />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/organizer/edit-event/:id"
                        element={
                            <ProtectedRoute allowedRoles={['ORGANIZER']}>
                                <EditEvent />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/organizer/reservations"
                        element={
                            <ProtectedRoute allowedRoles={['ORGANIZER']}>
                                <OrganizerReservations />
                            </ProtectedRoute>
                        }
                    />

                    {/* ===== ROUTES ADMIN ===== */}
                    <Route
                        path="/admin/manage-events"
                        element={
                            <ProtectedRoute allowedRoles={['ADMIN']}>
                                <ManageEvents />
                            </ProtectedRoute>
                        }
                    />

                    {/* ===== ROUTE 404 ===== */}
                    <Route path="*" element={<Navigate to="/" />} />

                </Routes>
            </div>
        </Router>
    );
}

export default App;