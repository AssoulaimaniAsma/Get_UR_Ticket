/*import React, { useState, useEffect } from 'react';
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

export default MyReservations;*/

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyReservations, getEventById, cancelReservation, getCurrentUser } from '../../services/api';
import './MyReservations.css';

function MyReservations() {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const navigate = useNavigate();
    const user = getCurrentUser();

    useEffect(() => {
        if (!user) { navigate('/login'); return; }
        loadReservations();
    }, []);

    const loadReservations = async () => {
        try {
            const data = await getMyReservations(user.id);
            // Enrichir avec les détails des événements
            const enriched = await Promise.all(
                data.map(async (res) => {
                    try {
                        const event = await getEventById(res.eventId);
                        return { ...res, event };
                    } catch {
                        return { ...res, event: null };
                    }
                })
            );
            setReservations(enriched);
        } catch (error) {
            console.error('Erreur:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (id) => {
        if (window.confirm('Voulez-vous annuler cette réservation ?')) {
            try {
                await cancelReservation(id);
                loadReservations();
            } catch (error) {
                alert('Erreur lors de l\'annulation');
            }
        }
    };

    const getStatusBadge = (statut) => {
        const styles = {
            CONFIRMED: { label: 'Confirmée',   color: '#27ae60', bg: '#d5f4e6' },
            PENDING:   { label: 'En attente',  color: '#f39c12', bg: '#fef5e7' },
            CANCELLED: { label: 'Annulée',     color: '#e74c3c', bg: '#fadbd8' },
        };
        return styles[statut] || { label: statut, color: '#666', bg: '#eee' };
    };

    const filtered = reservations.filter(r =>
        filter === 'ALL' ? true : r.statut === filter
    );

    if (loading) return <div className="loading">Chargement...</div>;

    return (
        <div className="container">
            <div className="page-header">
                <div>
                    <h1>Mes Réservations</h1>
                    <p>Gérez vos réservations d'événements</p>
                </div>
            </div>

            {/* Filtres */}
            <div className="filter-tabs" style={{ marginBottom: '1.5rem' }}>
                {['ALL', 'CONFIRMED', 'PENDING', 'CANCELLED'].map(f => (
                    <button
                        key={f}
                        className={filter === f ? 'active' : ''}
                        onClick={() => setFilter(f)}
                        style={{
                            padding: '0.5rem 1.25rem',
                            borderRadius: '20px',
                            border: '2px solid #667eea',
                            background: filter === f ? '#667eea' : 'white',
                            color: filter === f ? 'white' : '#667eea',
                            cursor: 'pointer',
                            marginRight: '0.5rem',
                            fontWeight: '600'
                        }}
                    >
                        {{ ALL: 'Toutes', CONFIRMED: '✅ Confirmées',
                           PENDING: '⏳ En attente', CANCELLED: '❌ Annulées' }[f]}
                        {' '}({f === 'ALL' ? reservations.length
                            : reservations.filter(r => r.statut === f).length})
                    </button>
                ))}
            </div>

            {/* Table */}
            {filtered.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">🎫</div>
                    <h3>Aucune réservation</h3>
                    <button className="btn btn-primary" onClick={() => navigate('/')}>
                        Découvrir les événements
                    </button>
                </div>
            ) : (
                <div className="table-container">
                    <table className="reservations-table">
                        <thead>
                            <tr>
                                <th>#ID</th>
                                <th>Événement</th>
                                <th>Date événement</th>
                                <th>Lieu</th>
                                <th>Places</th>
                                <th>Prix total</th>
                                <th>Réservé le</th>
                                <th>Statut</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filtered.map(reservation => {
                                const badge = getStatusBadge(reservation.statut);
                                return (
                                    <tr key={reservation.id}>
                                        <td>#{reservation.id}</td>
                                        <td>
                                            <strong>
                                                {reservation.event?.titre || `Event #${reservation.eventId}`}
                                            </strong>
                                            {reservation.event?.category && (
                                                <span style={{
                                                    display: 'block',
                                                    fontSize: '0.8rem',
                                                    color: '#666'
                                                }}>
                                                    {reservation.event.category.nom}
                                                </span>
                                            )}
                                        </td>
                                        <td>
                                            {reservation.event?.dateEvent
                                                ? new Date(reservation.event.dateEvent)
                                                    .toLocaleDateString('fr-FR')
                                                : '—'}
                                        </td>
                                        <td>{reservation.event?.lieu || '—'}</td>
                                        <td style={{ textAlign: 'center' }}>
                                            {reservation.nombrePlaces}
                                        </td>
                                        <td style={{ fontWeight: 'bold', color: '#27ae60' }}>
                                            {reservation.prixTotal === 0
                                                ? 'Gratuit'
                                                : `${reservation.prixTotal} DH`}
                                        </td>
                                        <td>
                                            {reservation.dateReservation
                                                ? new Date(reservation.dateReservation)
                                                    .toLocaleDateString('fr-FR')
                                                : '—'}
                                        </td>
                                        <td>
                                            <span style={{
                                                padding: '0.35rem 0.9rem',
                                                borderRadius: '15px',
                                                fontSize: '0.85rem',
                                                fontWeight: 'bold',
                                                color: badge.color,
                                                background: badge.bg
                                            }}>
                                                {badge.label}
                                            </span>
                                        </td>
                                        <td>
                                            {reservation.statut !== 'CANCELLED' && (
                                                <button
                                                    className="btn btn-danger"
                                                    style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem' }}
                                                    onClick={() => handleCancel(reservation.id)}
                                                >
                                                    Annuler
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default MyReservations;