import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyEvents, deleteEvent, getCurrentUser } from '../../services/api';
import EventCard from '../../components/EventCard';
import './MyEvents.css';

const MyEvents = () => {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const user = getCurrentUser();

    useEffect(() => {
        loadEvents();
    }, []);

    const loadEvents = async () => {
        try {
            const data = await getMyEvents(user.id);
            setEvents(data);
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors du chargement des événements');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Voulez-vous vraiment supprimer cet événement ?')) {
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

    const handleEdit = (id) => {
        navigate(`/organizer/edit-event/${id}`);
    };

    if (loading) return <div className="loading">Chargement...</div>;

    return (
        <div className="my-events-container">
            <div className="page-header">
                <h1>Mes Événements</h1>
                <button 
                    className="btn btn-primary" 
                    onClick={() => navigate('/organizer/create-event')}
                >
                    + Créer un événement
                </button>
            </div>

            {events.length === 0 ? (
                <div className="no-data">
                    <p>Vous n'avez pas encore créé d'événements</p>
                    <button 
                        className="btn btn-primary" 
                        onClick={() => navigate('/organizer/create-event')}
                    >
                        Créer mon premier événement
                    </button>
                </div>
            ) : (
                <div className="events-grid">
                    {events.map(event => (
                        <div key={event.id} className="event-card-wrapper">
                            <EventCard event={event} />
                            <div className="event-status">
                                <span className={`badge badge-${event.statut.toLowerCase()}`}>
                                    {event.statut === 'PENDING' && '⏳ En attente'}
                                    {event.statut === 'APPROVED' && '✅ Approuvé'}
                                    {event.statut === 'REJECTED' && '❌ Rejeté'}
                                </span>
                            </div>
                            <div className="event-actions">
                                <button 
                                    className="btn btn-secondary" 
                                    onClick={() => handleEdit(event.id)}
                                >
                                    ✏️ Modifier
                                </button>
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

export default MyEvents;