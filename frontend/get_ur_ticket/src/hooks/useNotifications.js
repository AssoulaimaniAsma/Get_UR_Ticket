import { useState, useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getCurrentUser } from '../services/api';
import useSound from './useSound';

const RESERVATION_WS_URL = 'http://localhost:7073/ws';
const EVENT_WS_URL       = 'http://localhost:7071/ws-events';
const STORAGE_KEY        = 'app_notifications';
const MAX_NOTIFICATIONS  = 50; // Maximum à garder

// ✅ Helpers localStorage
const loadFromStorage = () => {
    try {
        const saved = localStorage.getItem(STORAGE_KEY);
        return saved ? JSON.parse(saved) : [];
    } catch (e) {
        console.error('Erreur lecture notifications:', e);
        return [];
    }
};

const saveToStorage = (notifications) => {
    try {
        // Garder seulement les 50 dernières
        const toSave = notifications.slice(0, MAX_NOTIFICATIONS);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(toSave));
    } catch (e) {
        console.error('Erreur sauvegarde notifications:', e);
    }
};

const useNotifications = () => {
    // ✅ Initialiser depuis localStorage
    const [notifications, setNotifications] = useState(() => loadFromStorage());
    const [connected, setConnected]         = useState(false);
    const clientRef                         = useRef(null);
    const user                              = getCurrentUser();
    const { playNotificationSound, playErrorSound } = useSound();

    // ✅ Compter les non lues depuis localStorage
    const [unreadCount, setUnreadCount] = useState(() => {
        const saved = loadFromStorage();
        return saved.filter(n => !n.read).length;
    });

    // ✅ Sauvegarder dans localStorage à chaque changement
    useEffect(() => {
        saveToStorage(notifications);
    }, [notifications]);

    // ✅ Connexion WebSocket
    useEffect(() => {
        if (!user) return;

        if (user.role === 'ORGANIZER') connectToReservationWS();
        if (user.role === 'ADMIN')     connectToEventWS();

        return () => {
            if (clientRef.current?.active) {
                clientRef.current.deactivate();
            }
        };
    }, [user?.id]);

    const connectToReservationWS = () => {
        const token = localStorage.getItem('token');

        const client = new Client({
            webSocketFactory: () => new SockJS(RESERVATION_WS_URL),
            connectHeaders:   { Authorization: `Bearer ${token}` },
            reconnectDelay:   5000,
            debug:            (str) => console.log('🔌 WS Reservation:', str),

            onConnect: () => {
                console.log('✅ WebSocket Reservation connecté');
                setConnected(true);

                client.subscribe(
                    `/queue/notifications-${user.id}`,
                    (message) => handleMessage(message)
                );
            },
            onDisconnect:  () => setConnected(false),
            onStompError:  () => setConnected(false)
        });

        client.activate();
        clientRef.current = client;
    };

    const connectToEventWS = () => {
        const token = localStorage.getItem('token');

        const client = new Client({
            webSocketFactory: () => new SockJS(EVENT_WS_URL),
            connectHeaders:   { Authorization: `Bearer ${token}` },
            reconnectDelay:   5000,
            debug:            (str) => console.log('🔌 WS Event:', str),

            onConnect: () => {
                console.log('✅ WebSocket Event connecté (Admin)');
                setConnected(true);

                client.subscribe(
                    '/topic/admin-notifications',
                    (message) => handleMessage(message)
                );
            },
            onDisconnect:  () => setConnected(false),
            onStompError:  () => setConnected(false)
        });

        client.activate();
        clientRef.current = client;
    };

    // ✅ Gérer les messages reçus
    const handleMessage = (message) => {
        try {
            const notification = JSON.parse(message.body);
            console.log('🔔 Notification reçue:', notification);

            // Jouer le son
            if (notification.type === 'RESERVATION_ANNULEE') {
                playErrorSound();
            } else {
                playNotificationSound();
            }

            const newNotif = {
                ...notification,
                id:        `notif_${Date.now()}_${Math.random()}`,
                read:      false,
                receivedAt: new Date().toLocaleString('fr-FR')
            };

            // ✅ Ajouter et sauvegarder
            setNotifications(prev => {
                const updated = [newNotif, ...prev].slice(0, MAX_NOTIFICATIONS);
                saveToStorage(updated); // Sauvegarde immédiate
                return updated;
            });

            setUnreadCount(prev => prev + 1);

        } catch (e) {
            console.error('Erreur parsing notification:', e);
        }
    };

    // ✅ Marquer toutes comme lues
    const markAllRead = useCallback(() => {
        setNotifications(prev => {
            const updated = prev.map(n => ({ ...n, read: true }));
            saveToStorage(updated);
            return updated;
        });
        setUnreadCount(0);
    }, []);

    // ✅ Marquer une seule comme lue
    const markOneRead = useCallback((id) => {
        setNotifications(prev => {
            const updated = prev.map(n =>
                n.id === id ? { ...n, read: true } : n
            );
            saveToStorage(updated);
            return updated;
        });
        setUnreadCount(prev => Math.max(0, prev - 1));
    }, []);

    // ✅ Supprimer une notification
    const removeNotification = useCallback((id) => {
        setNotifications(prev => {
            const notif   = prev.find(n => n.id === id);
            const updated = prev.filter(n => n.id !== id);
            saveToStorage(updated);
            if (notif && !notif.read) {
                setUnreadCount(c => Math.max(0, c - 1));
            }
            return updated;
        });
    }, []);

    // ✅ Tout effacer
    const clearAll = useCallback(() => {
        setNotifications([]);
        setUnreadCount(0);
        localStorage.removeItem(STORAGE_KEY);
    }, []);

    return {
        notifications,
        unreadCount,
        connected,
        markAllRead,
        markOneRead,
        removeNotification,
        clearAll
    };
};

export default useNotifications;