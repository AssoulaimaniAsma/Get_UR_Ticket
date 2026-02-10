import React from 'react';
import { getCurrentUser } from '../services/api';
import { useNavigate } from 'react-router-dom';

function Profile() {
    const user = getCurrentUser();
    const navigate = useNavigate();

    if (!user) {
        navigate('/login');
        return null;
    }

    return (
        <div className="container">
            <div className="page-header">
                <h1>Mon Profil</h1>
                <p>Informations de votre compte</p>
            </div>

            <div className="event-details">
                <div className="event-details-info">
                    <div className="info-item">
                        <span className="info-label">👤 Nom:</span>
                        <span>{user.nom}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">✉️ Email:</span>
                        <span>{user.email}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">📱 Téléphone:</span>
                        <span>{user.telephone || 'Non renseigné'}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">📍 Adresse:</span>
                        <span>{user.adresse || 'Non renseignée'}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">🎭 Rôle:</span>
                        <span>{user.role}</span>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;