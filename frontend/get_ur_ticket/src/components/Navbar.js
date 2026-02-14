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

    const getRoleDisplay = (role) => {
        const roles = {
            'ADMIN': '👑 Admin',
            'ORGANIZER': '🎭 Organisateur',
            'USER': '👤 Utilisateur'
        };
        return roles[role] || role;
    };

    return (
        <nav className="navbar">
            <div className="navbar-content">
                <Link to="/" className="navbar-brand">
                    🎉 Event Booking
                </Link>
                
                {user ? (
                    <div className="navbar-links">
                        {/* Navigation pour tous */}
                        <Link to="/">🏠 Événements</Link>
                        
                        {/* Navigation USER */}
                        {user.role === 'USER' && (
                            <Link to="/my-reservations">🎫 Mes Réservations</Link>
                        )}
                        
                        {/* Navigation ORGANIZER */}
                        {user.role === 'ORGANIZER' && (
                            <>
                                <Link to="/organizer/my-events">📋 Mes Événements</Link>
                                <Link to="/organizer/create-event">➕ Créer Événement</Link>
                            </>
                        )}
                        
                        {/* Navigation ADMIN */}
                        {user.role === 'ADMIN' && (
                            <Link to="/admin/manage-events">⚙️ Gérer Événements</Link>
                        )}
                        
                        {/* Info utilisateur */}
                        <div className="user-info">
                            <span>{user.nom}</span>
                            <span className="user-role">{getRoleDisplay(user.role)}</span>
                        </div>
                        
                        <button onClick={handleLogout} className="btn-logout">
                            🚪 Déconnexion
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