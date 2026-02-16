package com.assoulaimani.reservationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Envoyer une notification à un organisateur spécifique
     * Canal: /queue/notifications-{organizerId}
     */
    public void sendNotificationToOrganizer(Long organizerId, Map<String, Object> notification) {
        String destination = "/queue/notifications-" + organizerId;
        System.out.println("📨 Envoi notification vers: " + destination);
        System.out.println("📋 Contenu: " + notification);

        messagingTemplate.convertAndSend(destination, notification);
        System.out.println("✅ Notification envoyée à l'organisateur: " + organizerId);
    }
    // ✅ Notification pour l'admin (nouvel événement créé)
    public void sendNotificationToAdmin(Map<String, Object> notification) {
        String destination = "/topic/admin-notifications";
        System.out.println("📨 Notification admin → " + destination);
        messagingTemplate.convertAndSend(destination, notification);
    }
    /**
     * Broadcast à tous les organisateurs
     */
    public void broadcastNotification(Map<String, Object> notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}