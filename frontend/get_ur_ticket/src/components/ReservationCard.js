import React from 'react';
import { cancelReservation } from '../services/api';

function ReservationCard({ reservation, onUpdate }) {
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const handleCancel = async () => {
        if (window.confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
            try {
                await cancelReservation(reservation.id);
                alert('Réservation annulée avec succès');
                onUpdate();
            } catch (error) {
                alert('Erreur lors de l\'annulation de la réservation');
            }
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case 'CONFIRMED':
                return 'status-confirmed';
            case 'PENDING':
                return 'status-pending';
            case 'CANCELLED':
                return 'status-cancelled';
            default:
                return '';
        }
    };

    return (
        <div className="reservation-card">
            <div className="reservation-header">
                <h3>Réservation #{reservation.id}</h3>
                <span className={`reservation-status ${getStatusClass(reservation.statut)}`}>
                    {reservation.statut}
                </span>
            </div>
            <p><strong>Nombre de places:</strong> {reservation.nombrePlaces}</p>
            <p><strong>Date de réservation:</strong> {formatDate(reservation.dateReservation)}</p>
            
            {reservation.statut !== 'CANCELLED' && (
                <div className="reservation-actions">
                    <button onClick={handleCancel} className="btn btn-danger">
                        Annuler la réservation
                    </button>
                </div>
            )}
        </div>
    );
}

export default ReservationCard;