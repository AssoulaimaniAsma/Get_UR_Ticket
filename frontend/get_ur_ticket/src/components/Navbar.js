import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { logout, getCurrentUser } from '../services/api';

function Navbar() {
    const navigate = useNavigate();
    const user = getCurrentUser();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-content">
                <Link to="/" className="navbar-brand">
                    🎉 Event Booking
                </Link>
                
                {user ? (
                    <div className="navbar-links">
                        <Link to="/">Événements</Link>
                        <Link to="/my-reservations">Mes Réservations</Link>
                        <Link to="/profile">Profil</Link>
                        <span>Bonjour, {user.nom}</span>
                        <button onClick={handleLogout} className="btn-logout">
                            Déconnexion
                        </button>
                    </div>
                ) : (
                    <div className="navbar-links">
                        <Link to="/login">Connexion</Link>
                        <Link to="/register">Inscription</Link>
                    </div>
                )}
            </div>
        </nav>
    );
}

export default Navbar;