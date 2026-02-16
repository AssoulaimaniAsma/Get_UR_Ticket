import React, { useState, useEffect } from 'react';
import { getOrganizerReservations, getCurrentUser } from '../../services/api';
import './OrganizerReservations.css';

function OrganizerReservations() {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const user = getCurrentUser();

    useEffect(() => {
        loadReservations();
    }, []);

    const loadReservations = async () => {
        try {
            const data = await getOrganizerReservations(user.id);
            setReservations(data);
        } catch (error) {
            console.error('Erreur:', error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (statut) => {
        const styles = {
            CONFIRMED: { label: 'Confirmée',  color: '#27ae60', bg: '#d5f4e6' },
            PENDING:   { label: 'En attente', color: '#f39c12', bg: '#fef5e7' },
            CANCELLED: { label: 'Annulée',    color: '#e74c3c', bg: '#fadbd8' },
        };
        return styles[statut] || { label: statut, color: '#666', bg: '#eee' };
    };

    const filtered = reservations.filter(r =>
        filter === 'ALL' ? true : r.statut === filter
    );

    // Stats
    const stats = {
        total: reservations.length,
        confirmed: reservations.filter(r => r.statut === 'CONFIRMED').length,
        cancelled: reservations.filter(r => r.statut === 'CANCELLED').length,
        revenue: reservations
            .filter(r => r.statut !== 'CANCELLED')
            .reduce((sum, r) => sum + (r.prixTotal || 0), 0)
    };

    if (loading) return <div className="loading">Chargement...</div>;

    return (
        <div className="container">
            <div className="page-header">
                <div>
                    <h1>Réservations de mes événements</h1>
                    <p>Suivez les réservations de vos événements</p>
                </div>
            </div>

            {/* Stats */}
            <div className="organizer-stats">
                <div className="stat-card">
                    <h3>Total réservations</h3>
                    <p>{stats.total}</p>
                </div>
                <div className="stat-card">
                    <h3>Confirmées</h3>
                    <p style={{ color: '#27ae60' }}>{stats.confirmed}</p>
                </div>
                <div className="stat-card">
                    <h3>Annulées</h3>
                    <p style={{ color: '#e74c3c' }}>{stats.cancelled}</p>
                </div>
                <div className="stat-card">
                    <h3>Revenus</h3>
                    <p style={{ color: '#667eea' }}>{stats.revenue} DH</p>
                </div>
            </div>

            {/* Filtres */}
            <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.5rem' }}>
                {['ALL', 'CONFIRMED', 'PENDING', 'CANCELLED'].map(f => (
                    <button
                        key={f}
                        onClick={() => setFilter(f)}
                        style={{
                            padding: '0.5rem 1.25rem',
                            borderRadius: '20px',
                            border: '2px solid #667eea',
                            background: filter === f ? '#667eea' : 'white',
                            color: filter === f ? 'white' : '#667eea',
                            cursor: 'pointer',
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
                    <div className="empty-state-icon">📋</div>
                    <h3>Aucune réservation</h3>
                </div>
            ) : (
                <div className="table-container">
                    <table className="reservations-table">
                        <thead>
                            <tr>
                                <th>#ID</th>
                                <th>Client</th>
                                <th>Événement</th>
                                <th>Date réservation</th>
                                <th>Places</th>
                                <th>Prix total</th>
                                <th>Statut</th>
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
                                                {reservation.user?.nom || `User #${reservation.userId}`}
                                            </strong>
                                            <span style={{
                                                display: 'block',
                                                fontSize: '0.8rem',
                                                color: '#666'
                                            }}>
                                                {reservation.user?.email || ''}
                                            </span>
                                        </td>
                                        <td>
                                            <strong>
                                                {reservation.event?.titre || `Event #${reservation.eventId}`}
                                            </strong>
                                        </td>
                                        <td>
                                            {reservation.dateReservation
                                                ? new Date(reservation.dateReservation)
                                                    .toLocaleDateString('fr-FR')
                                                : '—'}
                                        </td>
                                        <td style={{ textAlign: 'center' }}>
                                            {reservation.nombrePlaces}
                                        </td>
                                        <td style={{ fontWeight: 'bold', color: '#27ae60' }}>
                                            {reservation.prixTotal === 0
                                                ? 'Gratuit'
                                                : `${reservation.prixTotal} DH`}
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

export default OrganizerReservations;