import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ReservationCard from '../components/ReservationCard';
import { getMyReservations, getCurrentUser } from '../services/api';

function MyReservations() {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const user = getCurrentUser();

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }
        loadReservations();
    }, []);

    const loadReservations = async () => {
        try {
            const data = await getMyReservations(user.id);
            setReservations(data);
            console.log(data);
            setLoading(false);
        } catch (error) {
            console.error('Erreur lors du chargement des réservations:', error);
            setLoading(false);
        }
    };

    if (loading) {
        return <div className="loading">Chargement de vos réservations...</div>;
    }

    return (
        <div className="container">
            <div className="page-header">
                <h1>Mes Réservations</h1>
                <p>Gérez vos réservations d'événements</p>
            </div>

            <div className="reservations-list">
                {reservations.length > 0 ? (
                    reservations.map(reservation => (
                        <ReservationCard
                            key={reservation.id}
                            reservation={reservation}
                            onUpdate={loadReservations}
                        />
                    ))
                ) : (
                    <p>Vous n'avez aucune réservation pour le moment</p>
                )}
            </div>
        </div>
    );
}

export default MyReservations;