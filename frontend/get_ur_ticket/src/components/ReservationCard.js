import React from 'react';
import './ReservationCard.css';

const ReservationCard = ({ reservation, onCancel, onConfirm }) => {
    const getStatusBadge = (statut) => {
        const badges = {
            EN_ATTENTE: { label: 'En attente', class: 'badge-warning' },
            CONFIRMEE: { label: 'Confirmée', class: 'badge-success' },
            ANNULEE: { label: 'Annulée', class: 'badge-danger' }
        };
        return badges[statut] || { label: statut, class: 'badge-default' };
    };

    const badge = getStatusBadge(reservation.statut);

    return (
        <div className="reservation-card">
            <div className="reservation-header">
                <h3>{reservation.event?.titre || 'Événement'}</h3>
                <span className={`badge ${badge.class}`}>{badge.label}</span>
            </div>
            
            <div className="reservation-body">
                <p><strong>📅 Date:</strong> {new Date(reservation.event?.dateEvent).toLocaleString('fr-FR')}</p>
                <p><strong>📍 Lieu:</strong> {reservation.event?.lieu}</p>
                <p><strong>🎫 Places réservées:</strong> {reservation.nombrePlaces}</p>
                <p><strong>💰 Prix total:</strong> {reservation.prixTotal} DH</p>
                <p><strong>📆 Réservé le:</strong> {new Date(reservation.dateReservation).toLocaleDateString('fr-FR')}</p>
            </div>

            {reservation.statut === 'EN_ATTENTE' && (
                <div className="reservation-actions">
                    {onConfirm && (
                        <button onClick={() => onConfirm(reservation.id)} className="btn btn-success">
                            Confirmer
                        </button>
                    )}
                    {onCancel && (
                        <button onClick={() => onCancel(reservation.id)} className="btn btn-danger">
                            Annuler
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default ReservationCard;