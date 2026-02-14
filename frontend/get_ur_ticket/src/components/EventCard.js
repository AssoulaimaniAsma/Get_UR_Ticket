import React from 'react';

function EventCard({ event, onClick, showActions, onEdit, onDelete }) {
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

    const getStatusBadge = (statut) => {
        const badges = {
            PENDING: { class: 'status-pending', text: '⏳ En attente' },
            APPROVED: { class: 'status-approved', text: '✅ Approuvé' },
            REJECTED: { class: 'status-rejected', text: '❌ Rejeté' }
        };
        const badge = badges[statut] || badges.PENDING;
        return <span className={`reservation-status ${badge.class}`}>{badge.text}</span>;
    };

    return (
        <div className="event-card" onClick={onClick}>
            <div className="event-card-image">
                {event.imageUrl ? (
                    <img src={event.imageUrl} alt={event.titre} style={{width: '100%', height: '100%', objectFit: 'cover'}} />
                ) : (
                    <span>🎭</span>
                )}
            </div>
            <div className="event-card-content">
                <h3 className="event-card-title">{event.titre}</h3>
                
                {event.statut && showActions && getStatusBadge(event.statut)}
                
                <p className="event-card-info">📍 {event.lieu}</p>
                <p className="event-card-info">📅 {formatDate(event.dateEvent)}</p>
                <p className="event-card-info">
                    👥 {event.capaciteDisponible} / {event.capaciteTotal} places
                </p>
                
                {event.category && (
                    <span className="event-card-category">
                        {event.category.iconUrl} {event.category.nom}
                    </span>
                )}
                
                <div className="event-card-price">
                    {event.prix === 0 ? '🎁 Gratuit' : `💰 ${event.prix} DH`}
                </div>

                {showActions && (
                    <div className="event-card-actions" onClick={(e) => e.stopPropagation()}>
                        <button onClick={() => onEdit(event)} className="btn btn-warning">
                            ✏️ Modifier
                        </button>
                        <button onClick={() => onDelete(event.id)} className="btn btn-danger">
                            🗑️ Supprimer
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default EventCard;