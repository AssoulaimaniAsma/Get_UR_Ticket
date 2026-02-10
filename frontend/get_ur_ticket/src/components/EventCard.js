import React from 'react';
import { useNavigate } from 'react-router-dom';

function EventCard({ event }) {
    const navigate = useNavigate();

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

    return (
        <div className="event-card" onClick={() => navigate(`/event/${event.id}`)}>
            <div className="event-card-image">
                {event.imageUrl ? (
                    <img src={event.imageUrl} alt={event.titre} />
                ) : (
                    <span>🎭</span>
                )}
            </div>
            <div className="event-card-content">
                <h3 className="event-card-title">{event.titre}</h3>
                <p className="event-card-info">📍 {event.lieu}</p>
                <p className="event-card-info">📅 {formatDate(event.dateEvent)}</p>
                <p className="event-card-info">
                    👥 {event.capaciteDisponible} / {event.capaciteTotal} places
                </p>
                {event.category && (
                    <span className="event-card-category">
                        {event.category.nom}
                    </span>
                )}
                <div className="event-card-price">
                    {event.prix === 0 ? 'Gratuit' : `${event.prix} DH`}
                </div>
            </div>
        </div>
    );
}

export default EventCard;