import React, { useState, useEffect } from 'react';
import { getMyReservations, cancelReservation, confirmReservation, getCurrentUser } from '../../services/api';
import ReservationCard from '../../components/ReservationCard';
import './MyReservations.css';

const MyReservations = () => {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const user = getCurrentUser();

    useEffect(() => {
        loadReservations();
    }, []);

    const loadReservations = async () => {
        try {
            const data = await getMyReservations(user.id);
            setReservations(data);
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors du chargement des réservations');
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (id) => {
        if (window.confirm('Voulez-vous vraiment annuler cette réservation ?')) {
            try {
                await cancelReservation(id);
                alert('Réservation annulée avec succès');
                loadReservations();
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors de l\'annulation');
            }
        }
    };

    const handleConfirm = async (id) => {
        try {
            await confirmReservation(id);
            alert('Réservation confirmée avec succès');
            loadReservations();
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors de la confirmation');
        }
    };

    const filteredReservations = reservations.filter(res => {
        if (filter === 'ALL') return true;
        return res.statut === filter;
    });

    if (loading) return <div className="loading">Chargement...</div>;

    return (
        <div className="my-reservations-container">
            <h1>Mes Réservations</h1>

            <div className="filter-tabs">
                <button 
                    className={filter === 'ALL' ? 'active' : ''} 
                    onClick={() => setFilter('ALL')}
                >
                    Toutes ({reservations.length})
                </button>
                <button 
                    className={filter === 'EN_ATTENTE' ? 'active' : ''} 
                    onClick={() => setFilter('EN_ATTENTE')}
                >
                    En attente ({reservations.filter(r => r.statut === 'EN_ATTENTE').length})
                </button>
                <button 
                    className={filter === 'CONFIRMEE' ? 'active' : ''} 
                    onClick={() => setFilter('CONFIRMEE')}
                >
                    Confirmées ({reservations.filter(r => r.statut === 'CONFIRMEE').length})
                </button>
                <button 
                    className={filter === 'ANNULEE' ? 'active' : ''} 
                    onClick={() => setFilter('ANNULEE')}
                >
                    Annulées ({reservations.filter(r => r.statut === 'ANNULEE').length})
                </button>
            </div>

            <div className="reservations-grid">
                {filteredReservations.length === 0 ? (
                    <p className="no-data">Aucune réservation trouvée</p>
                ) : (
                    filteredReservations.map(reservation => (
                        <ReservationCard
                            key={reservation.id}
                            reservation={reservation}
                            onCancel={handleCancel}
                            onConfirm={handleConfirm}
                        />
                    ))
                )}
            </div>
        </div>
    );
};

export default MyReservations;