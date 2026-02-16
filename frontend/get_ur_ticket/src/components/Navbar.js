import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { logout, getCurrentUser } from '../services/api';
import NotificationBell from './NotificationBell';

function Navbar() {
    const navigate = useNavigate();
    const user = getCurrentUser();

    const handleLogout = () => {
        logout();
        navigate('/login');
        window.location.reload();
    };

    const getRoleDisplay = (role) => {
        const roles = {
            'ADMIN':     '👑 Admin',
            'ORGANIZER': '🎭 Organisateur',
            'USER':      '👤 Utilisateur'
        };
        return roles[role] || role;
    };

    return (
        <nav className="navbar">
            <div className="navbar-content">

                <Link to="/" className="navbar-brand">Get Your Ticket</Link>

                {user ? (
                    <div className="navbar-links">
                        <Link to="/">🏠 Événements</Link>

                        {/* USER */}
                        {user.role === 'USER' && (
                            <>
                                <Link to="/my-reservations">🎫 Mes Réservations</Link>
                                <Link to="/profile">👤 Mon Profil</Link>
                            </>
                        )}

                        {/* ORGANIZER */}
                        {user.role === 'ORGANIZER' && (
                            <>
                                <Link to="/organizer/my-events">📋 Mes Événements</Link>
                                <Link to="/organizer/create-event">➕ Créer</Link>
                                <Link to="/organizer/reservations">📊 Réservations</Link>
                                <Link to="/profile">👤 Mon Profil</Link>
                            </>
                        )}

                        {/* ADMIN */}
                        {user.role === 'ADMIN' && (
                            <>
                                <Link to="/admin/manage-events">⚙️ Gérer Événements</Link>
                                <Link to="/profile">👤 Mon Profil</Link>
                            </>
                        )}

                        {/* ✅ Cloche UNIQUEMENT pour ORGANIZER */}
                       {(user.role === 'ORGANIZER' || user.role === 'ADMIN') && (
    <NotificationBell />
)}

                        

                        <button onClick={handleLogout} className="btn-logout">
                            🚪 Déconnexion
                        </button>
                    </div>
                ) : (
                    <div className="navbar-links">
                        <Link to="/">🏠 Événements</Link>
                        <Link to="/login">🔐 Connexion</Link>
                        <Link to="/register">📝 Inscription</Link>
                    </div>
                )}
            </div>
        </nav>
    );
}

export default Navbar;