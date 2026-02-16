import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getEventById, createReservation, getCurrentUser,getUserById } from '../services/api';

function EventDetails() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [nombrePlaces, setNombrePlaces] = useState(1);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState('');
    const user = getCurrentUser();
    const [organizer, setOrganizer] = useState(null);

    useEffect(() => {
        loadEvent();
    }, [id]);

    const loadEvent = async () => {
        try {
            const data = await getEventById(id);
            console.log(data);
            setEvent(data);
             if (data.organisateurId) {
            const user = await getUserById(data.organisateurId);
            console.log(user);
            setOrganizer(user);
            }   
            setLoading(false);
        } catch (error) {
            console.error('Erreur lors du chargement de l\'événement:', error);
            setLoading(false);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            weekday: 'long',
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const handleReservation = async (e) => {
        e.preventDefault();
        setMessage('');

        if (!user) {
            navigate('/login');
            return;
        }

        try {
            await createReservation({
                eventId: event.id,
                userId: user.id,
                nombrePlaces: nombrePlaces,
                dateReservation: new Date().toISOString(),
                statut: 'PENDING'
            });
            setMessage('Réservation effectuée avec succès !');
            setTimeout(() => {
                navigate('/my-reservations');
            }, 2000);
        } catch (error) {
            setMessage('Erreur lors de la réservation. Veuillez réessayer.');
        }
    };

    if (loading) {
        return <div className="loading">Chargement...</div>;
    }

    if (!event) {
        return <div className="container">Événement non trouvé</div>;
    }

    return (
        <div className="container">
            <div className="event-details">
                <div className="event-details-header">
                    <h1>{event.titre}</h1>
                    {event.category && (
                        <span className="event-card-category">
                            {event.category.nom} 
                        </span>
                    )}
                    {organizer && (
    <div className="event-card-category">
        <span >👤 Organisateur: </span>
        <span>{organizer.nom}</span>
    </div>
)}

                </div>

                <div className="event-details-info">
                    <div className="info-item">
                        <span className="info-label">📅 Date:</span>
                        <span>{formatDate(event.dateEvent)}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">📍 Lieu:</span>
                        <span>{event.lieu}</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">👥 Capacité:</span>
                        <span>{event.capaciteDisponible} / {event.capaciteTotal} places disponibles</span>
                    </div>
                    <div className="info-item">
                        <span className="info-label">💰 Prix:</span>
                        <span>{event.prix === 0 ? 'Gratuit' : `${event.prix} DH`}</span>
                    </div>
                </div>

                <div>
                    <h3>Description</h3>
                    <p>{event.description || 'Pas de description disponible'}</p>
                </div>

                {event.capaciteDisponible > 0 ? (
                    <div className="reservation-form">
                        <h3>Réserver des places</h3>
                        {message && (
                            <div className={message.includes('succès') ? 'success-message' : 'error-message'}>
                                {message}
                            </div>
                        )}
                        <form onSubmit={handleReservation}>
                            <div className="form-group">
                                <label>Nombre de places</label>
                                <input
                                    type="number"
                                    min="1"
                                    max={event.capaciteDisponible}
                                    value={nombrePlaces}
                                    onChange={(e) => setNombrePlaces(parseInt(e.target.value))}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <p><strong>Total:</strong> {event.prix * nombrePlaces} DH</p>
                            </div>
                            <button type="submit" className="btn btn-success">
                                Réserver maintenant
                            </button>
                        </form>
                    </div>
                ) : (
                    <div className="error-message">
                        Désolé, cet événement est complet
                    </div>
                )}
            </div>
        </div>
    );
}

export default EventDetails;