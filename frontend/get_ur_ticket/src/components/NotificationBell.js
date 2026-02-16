import React, { useState } from 'react';
import useNotifications from '../hooks/useNotifications';
import './NotificationBell.css';

const NotificationBell = () => {
    const {
        notifications, unreadCount, connected,
        markAllRead, markOneRead,
        removeNotification, clearAll
    } = useNotifications();

    const [isOpen, setIsOpen] = useState(false);

    const togglePanel = () => {
        const opening = !isOpen;
        setIsOpen(opening);
        if (opening && unreadCount > 0) markAllRead();
    };

    const getNotifConfig = (type) => {
        const configs = {
            NOUVELLE_RESERVATION: {
                icon: '🎫', title: 'Nouvelle réservation !',
                color: '#27ae60', bg: '#d5f4e6'
            },
            RESERVATION_ANNULEE: {
                icon: '❌', title: 'Réservation annulée',
                color: '#e74c3c', bg: '#fadbd8'
            },
            NOUVEL_EVENEMENT: {
                icon: '🎉', title: 'Nouvel événement à approuver !',
                color: '#f39c12', bg: '#fef5e7'
            }
        };
        return configs[type] || {
            icon: '🔔', title: 'Notification',
            color: '#667eea', bg: '#f0f4ff'
        };
    };

    const renderContent = (notif) => {
        switch (notif.type) {
            case 'NOUVELLE_RESERVATION':
                return (
                    <>
                        <p>
                            <b>{notif.clientNom}</b> a réservé{' '}
                            <b>{notif.nombrePlaces} place(s)</b> pour{' '}
                            <b>{notif.eventTitre}</b>
                        </p>
                        {notif.prixTotal > 0 && (
                            <span className="notif-price">💰 {notif.prixTotal} DH</span>
                        )}
                    </>
                );
            case 'RESERVATION_ANNULEE':
                return (
                    <p>
                        <b>{notif.clientNom}</b> a annulé{' '}
                        <b>{notif.nombrePlaces} place(s)</b> pour{' '}
                        <b>{notif.eventTitre}</b>
                    </p>
                );
            case 'NOUVEL_EVENEMENT':
                return (
                    <p>
                        Nouvel événement <b>"{notif.eventTitre}"</b>{' '}
                        à <b>{notif.eventLieu}</b> en attente d'approbation
                    </p>
                );
            default:
                return <p>{notif.message || 'Nouvelle notification'}</p>;
        }
    };

    return (
        <div className="notification-wrapper">

            {/* Cloche */}
            <button className="notification-bell" onClick={togglePanel}>
                🔔
                {unreadCount > 0 && (
                    <span className="notification-badge">
                        {unreadCount > 9 ? '9+' : unreadCount}
                    </span>
                )}
                <span className={`connection-dot ${connected ? 'online' : 'offline'}`} />
            </button>

            {isOpen && (
                <>
                    <div className="notification-panel">

                        {/* Header */}
                        <div className="notification-header">
                            <div>
                                <h3>🔔 Notifications</h3>
                                <span className={`status-text ${connected ? 'online' : 'offline'}`}>
                                    {connected ? '● En ligne' : '○ Hors ligne'}
                                </span>
                            </div>
                            <div style={{ display: 'flex', gap: '0.5rem' }}>
                                {notifications.length > 0 && (
                                    <button className="clear-all-btn" onClick={clearAll}>
                                        Tout effacer
                                    </button>
                                )}
                            </div>
                        </div>

                        {/* Compteur */}
                        {notifications.length > 0 && (
                            <div className="notification-count">
                                {notifications.length} notification(s) —{' '}
                                {notifications.filter(n => !n.read).length} non lue(s)
                            </div>
                        )}

                        {/* Liste */}
                        <div className="notification-list">
                            {notifications.length === 0 ? (
                                <div className="no-notifications">
                                    <span style={{ fontSize: '2.5rem' }}>🔕</span>
                                    <p>Aucune notification</p>
                                </div>
                            ) : (
                                notifications.map(notif => {
                                    const config = getNotifConfig(notif.type);
                                    return (
                                        <div
                                            key={notif.id}
                                            className={`notification-item ${!notif.read ? 'unread' : ''}`}
                                            style={{ borderLeftColor: config.color }}
                                            onClick={() => !notif.read && markOneRead(notif.id)}
                                        >
                                            <div
                                                className="notif-icon-wrapper"
                                                style={{ background: config.bg }}
                                            >
                                                {config.icon}
                                            </div>

                                            <div className="notif-body">
                                                <strong style={{ color: config.color }}>
                                                    {config.title}
                                                </strong>
                                                {renderContent(notif)}
                                                <span className="notif-time">
                                                    🕐 {notif.receivedAt
                                                        || notif.dateReservation
                                                        || notif.dateCreation}
                                                </span>
                                            </div>

                                            <button
                                                className="notif-close"
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    removeNotification(notif.id);
                                                }}
                                            >
                                                ✕
                                            </button>
                                        </div>
                                    );
                                })
                            )}
                        </div>
                    </div>

                    <div className="notification-overlay" onClick={() => setIsOpen(false)} />
                </>
            )}
        </div>
    );
};

export default NotificationBell;