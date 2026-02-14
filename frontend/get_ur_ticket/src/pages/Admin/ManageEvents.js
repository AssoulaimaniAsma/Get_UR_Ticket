import React, { useState, useEffect } from 'react';
import { getAllEventsAdmin, approveEvent, rejectEvent, deleteEvent } from '../../services/api';
import EventCard from '../../components/EventCard';
import './ManageEvents.css';

const ManageEvents = () => {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');

    useEffect(() => {
        loadEvents();
    }, []);

    const loadEvents = async () => {
        try {
            const data = await getAllEventsAdmin();
            setEvents(data);
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors du chargement des événements');
        } finally {
            setLoading(false);
        }
    };

    const handleApprove = async (id) => {
        if (window.confirm('Voulez-vous approuver cet événement ?')) {
            try {
                await approveEvent(id);
                alert('Événement approuvé avec succès');
                loadEvents();
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors de l\'approbation');
            }
        }
    };

    const handleReject = async (id) => {
        if (window.confirm('Voulez-vous rejeter cet événement ?')) {
            try {
                await rejectEvent(id);
                alert('Événement rejeté avec succès');
                loadEvents();
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors du rejet');
            }
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Voulez-vous vraiment supprimer cet événement ? Cette action est irréversible.')) {
            try {
                await deleteEvent(id);
                alert('Événement supprimé avec succès');
                loadEvents();
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors de la suppression');
            }
        }
    };

    const filteredEvents = events.filter(event => {
        if (filter === 'ALL') return true;
        return event.statut === filter;
    });

    if (loading) return <div className="loading">Chargement...</div>;

    return (
        <div className="manage-events-container">
            <h1>Gestion des Événements (Admin)</h1>

            <div className="filter-tabs">
                <button 
                    className={filter === 'ALL' ? 'active' : ''} 
                    onClick={() => setFilter('ALL')}
                >
                    Tous ({events.length})
                </button>
                <button 
                    className={filter === 'PENDING' ? 'active' : ''} 
                    onClick={() => setFilter('PENDING')}
                >
                    En attente ({events.filter(e => e.statut === 'PENDING').length})
                </button>
                <button 
                    className={filter === 'APPROVED' ? 'active' : ''} 
                    onClick={() => setFilter('APPROVED')}
                >
                    Approuvés ({events.filter(e => e.statut === 'APPROVED').length})
                </button>
                <button 
                    className={filter === 'REJECTED' ? 'active' : ''} 
                    onClick={() => setFilter('REJECTED')}
                >
                    Rejetés ({events.filter(e => e.statut === 'REJECTED').length})
                </button>
            </div>

            {filteredEvents.length === 0 ? (
                <p className="no-data">Aucun événement trouvé</p>
            ) : (
                <div className="events-grid">
                    {filteredEvents.map(event => (
                        <div key={event.id} className="admin-event-card">
                            <EventCard event={event} />
                            
                            <div className="event-info">
                                <p><strong>Organisateur ID:</strong> {event.organisateurId}</p>
                                <p><strong>Statut:</strong> 
                                    <span className={`badge badge-${event.statut.toLowerCase()}`}>
                                        {event.statut === 'PENDING' && '⏳ En attente'}
                                        {event.statut === 'APPROVED' && '✅ Approuvé'}
                                        {event.statut === 'REJECTED' && '❌ Rejeté'}
                                    </span>
                                </p>
                                <p><strong>Créé le:</strong> {new Date(event.createdAt).toLocaleDateString('fr-FR')}</p>
                            </div>

                            <div className="admin-actions">
                                {event.statut === 'PENDING' && (
                                    <>
                                        <button 
                                            className="btn btn-success" 
                                            onClick={() => handleApprove(event.id)}
                                        >
                                            ✅ Approuver
                                        </button>
                                        <button 
                                            className="btn btn-warning" 
                                            onClick={() => handleReject(event.id)}
                                        >
                                            ❌ Rejeter
                                        </button>
                                    </>
                                )}
                                {event.statut === 'APPROVED' && (
                                    <button 
                                        className="btn btn-warning" 
                                        onClick={() => handleReject(event.id)}
                                    >
                                        ❌ Rejeter
                                    </button>
                                )}
                                {event.statut === 'REJECTED' && (
                                    <button 
                                        className="btn btn-success" 
                                        onClick={() => handleApprove(event.id)}
                                    >
                                        ✅ Approuver
                                    </button>
                                )}
                                <button 
                                    className="btn btn-danger" 
                                    onClick={() => handleDelete(event.id)}
                                >
                                    🗑️ Supprimer
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ManageEvents;