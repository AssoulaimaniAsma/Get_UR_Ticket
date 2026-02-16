////package com.assoulaimani.reservationservice.controller;
////
////
////import com.assoulaimani.reservationservice.client.EventClient;
////import com.assoulaimani.reservationservice.client.UserClient;
////import com.assoulaimani.reservationservice.entity.Reservation;
////import com.assoulaimani.reservationservice.entity.ReservationStatus;
////import com.assoulaimani.reservationservice.repository.ReservationRepository;
////import com.assoulaimani.reservationservice.model.*;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////@RestController
////@RequestMapping("/api/reservations")
////public class ReservationController {
////
////    @Autowired
////    private ReservationRepository reservationRepository;
////
////    @Autowired
////    private EventClient eventClient;
////
////    @Autowired
////    private UserClient userClient;
////
////    @GetMapping
////    public ResponseEntity<List<Reservation>> getAllReservations() {
////        return ResponseEntity.ok(reservationRepository.findAll());
////    }
////
////    @GetMapping("/{id}")
////    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
////        return reservationRepository.findById(id)
////                .map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    // Récupérer une réservation avec détails Event et User
////    @GetMapping("/{id}/details")
////    public ResponseEntity<Map<String, Object>> getReservationDetails(@PathVariable Long id) {
////        return reservationRepository.findById(id)
////                .map(reservation -> {
////                    Map<String, Object> details = new HashMap<>();
////                    details.put("reservation", reservation);
////
////                    try {
////                        details.put("event", eventClient.getEventById(reservation.getEventId()));
////                    } catch (Exception e) {
////                        details.put("event", null);
////                    }
////
////                    try {
////                        details.put("user", userClient.getUserById(reservation.getUserId()));
////                    } catch (Exception e) {
////                        details.put("user", null);
////                    }
////
////                    return ResponseEntity.ok(details);
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @GetMapping("/user/{userId}")
////    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
////        return ResponseEntity.ok(reservationRepository.findByUserId(userId));
////    }
////
////    @GetMapping("/event/{eventId}")
////    public ResponseEntity<List<Reservation>> getReservationsByEvent(@PathVariable Long eventId) {
////        return ResponseEntity.ok(reservationRepository.findByEventId(eventId));
////    }
////
////    @GetMapping("/event/{eventId}/places-reservees")
////    public ResponseEntity<Integer> getPlacesReserveesByEvent(@PathVariable Long eventId) {
////        Integer places = reservationRepository.countPlacesReserveesByEventId(eventId);
////        return ResponseEntity.ok(places != null ? places : 0);
////    }
////
////    // Vérifier la disponibilité avant de créer une réservation
////    @PostMapping
////    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
////        try {
////            // Vérifier que l'événement existe
////            var event = eventClient.getEventById(reservation.getEventId());
////            if (event == null) {
////                return ResponseEntity.badRequest().body("Event non trouvé");
////            }
////
////            // Vérifier que l'utilisateur existe
////            var user = userClient.getUserById(reservation.getUserId());
////            if (user == null) {
////                return ResponseEntity.badRequest().body("User non trouvé");
////            }
////
////            // Vérifier la disponibilité
////            Integer placesReservees = reservationRepository.countPlacesReserveesByEventId(reservation.getEventId());
////            placesReservees = placesReservees != null ? placesReservees : 0;
////
////            if (event.getCapaciteTotal() != null &&
////                    (placesReservees + reservation.getNombrePlaces()) > event.getCapaciteTotal()) {
////                return ResponseEntity.badRequest().body("Capacité insuffisante");
////            }
////
////            Reservation savedReservation = reservationRepository.save(reservation);
////            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
////
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
////                    .body("Erreur lors de la communication avec les services");
////        }
////    }
////
////    @PutMapping("/{id}")
////    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
////        return reservationRepository.findById(id)
////                .map(existingReservation -> {
////                    existingReservation.setEventId(reservation.getEventId());
////                    existingReservation.setUserId(reservation.getUserId());
////                    existingReservation.setNombrePlaces(reservation.getNombrePlaces());
////                    existingReservation.setStatut(reservation.getStatut());
////                    return ResponseEntity.ok(reservationRepository.save(existingReservation));
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @PutMapping("/{id}/confirmer")
////    public ResponseEntity<Reservation> confirmerReservation(@PathVariable Long id) {
////        return reservationRepository.findById(id)
////                .map(reservation -> {
////                    reservation.setStatut(ReservationStatus.CONFIRMED);
////                    return ResponseEntity.ok(reservationRepository.save(reservation));
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @PutMapping("/{id}/annuler")
////    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id) {
////        return reservationRepository.findById(id)
////                .map(reservation -> {
////                    reservation.setStatut(ReservationStatus.CANCELLED);
////                    return ResponseEntity.ok(reservationRepository.save(reservation));
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
////        if (reservationRepository.existsById(id)) {
////            reservationRepository.deleteById(id);
////            return ResponseEntity.noContent().build();
////        }
////        return ResponseEntity.notFound().build();
////    }
////}
//package com.assoulaimani.reservationservice.controller;
//
//import com.assoulaimani.reservationservice.client.EventClient;
//import com.assoulaimani.reservationservice.client.UserClient;
//import com.assoulaimani.reservationservice.entity.Reservation;
//import com.assoulaimani.reservationservice.entity.ReservationStatus;
//import com.assoulaimani.reservationservice.repository.ReservationRepository;
//import com.assoulaimani.reservationservice.model.Event;
//import com.assoulaimani.reservationservice.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/reservations")
////@CrossOrigin(origins = "http://localhost:3000") // ✅ AJOUT CORS
//public class ReservationController {
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private EventClient eventClient;
//
//    @Autowired
//    private UserClient userClient;
//
//    @GetMapping
//    public ResponseEntity<List<Reservation>> getAllReservations() {
//        return ResponseEntity.ok(reservationRepository.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
//        return reservationRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // Récupérer une réservation avec détails Event et User
//    @GetMapping("/{id}/details")
//    public ResponseEntity<Map<String, Object>> getReservationDetails(@PathVariable Long id) {
//        return reservationRepository.findById(id)
//                .map(reservation -> {
//                    Map<String, Object> details = new HashMap<>();
//                    details.put("reservation", reservation);
//
//                    try {
//                        details.put("event", eventClient.getEventById(reservation.getEventId()));
//                    } catch (Exception e) {
//                        System.err.println("Erreur récupération event: " + e.getMessage());
//                        details.put("event", null);
//                    }
//
//                    try {
//                        details.put("user", userClient.getUserById(reservation.getUserId()));
//                    } catch (Exception e) {
//                        System.err.println("Erreur récupération user: " + e.getMessage());
//                        details.put("user", null);
//                    }
//
//                    return ResponseEntity.ok(details);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Map<String, Object>>> getReservationsByUser(
//            @PathVariable Long userId) {
//
//        List<Reservation> reservations = reservationRepository.findByUserId(userId);
//
//        // Enrichir chaque réservation avec les détails de l'événement
//        List<Map<String, Object>> enrichedReservations = reservations.stream()
//                .map(reservation -> {
//                    Map<String, Object> enriched = new HashMap<>();
//                    enriched.put("id", reservation.getId());
//                    enriched.put("eventId", reservation.getEventId());
//                    enriched.put("userId", reservation.getUserId());
//                    enriched.put("nombrePlaces", reservation.getNombrePlaces());
//                    enriched.put("statut", reservation.getStatut());
//                    enriched.put("dateReservation", reservation.getDateReservation());
//                    enriched.put("prixTotal", reservation.getPrixTotal());
//
//                    // ✅ Ajouter les détails de l'événement
//                    try {
//                        Event event = eventClient.getEventById(reservation.getEventId());
//                        enriched.put("event", event);
//                    } catch (Exception e) {
//                        System.err.println("Erreur récupération event: " + e.getMessage());
//                        enriched.put("event", null);
//                    }
//
//                    return enriched;
//                })
//                .collect(java.util.stream.Collectors.toList());
//
//        return ResponseEntity.ok(enrichedReservations);
//    }
//
//    @GetMapping("/event/{eventId}")
//    public ResponseEntity<List<Reservation>> getReservationsByEvent(@PathVariable Long eventId) {
//        return ResponseEntity.ok(reservationRepository.findByEventId(eventId));
//    }
//
//    @GetMapping("/event/{eventId}/places-reservees")
//    public ResponseEntity<Integer> getPlacesReserveesByEvent(@PathVariable Long eventId) {
//        Integer places = reservationRepository.countPlacesReserveesByEventId(eventId);
//        return ResponseEntity.ok(places != null ? places : 0);
//    }
//
//    // ✅ CORRIGÉ: Vérifier la disponibilité avant de créer une réservation
////    @PostMapping
////    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
////        System.out.println("=== CRÉATION RÉSERVATION ===");
////        System.out.println("Event ID: " + reservation.getEventId());
////        System.out.println("User ID: " + reservation.getUserId());
////        System.out.println("Nombre places: " + reservation.getNombrePlaces());
////        System.out.println("Statut: " + reservation.getStatut());
////
////        try {
////            // 1. Vérifier que l'événement existe
////            Event event = eventClient.getEventById(reservation.getEventId());
////            if (event == null) {
////                System.err.println("ERREUR: Event non trouvé");
////                return ResponseEntity.badRequest().body("Event non trouvé");
////            }
////            System.out.println("Event trouvé: " + event.getTitre());
////
////            // 2. Vérifier que l'utilisateur existe
////            User user = userClient.getUserById(reservation.getUserId());
////            if (user == null) {
////                System.err.println("ERREUR: User non trouvé");
////                return ResponseEntity.badRequest().body("User non trouvé");
////            }
////            System.out.println("User trouvé: " + user.getNom());
////
////            // 3. Vérifier la capacité disponible
////            System.out.println("Capacité disponible de l'event: " + event.getCapaciteDisponible());
////            System.out.println("Places demandées: " + reservation.getNombrePlaces());
////
////            if (event.getCapaciteDisponible() == null ||
////                    event.getCapaciteDisponible() < reservation.getNombrePlaces()) {
////                System.err.println("ERREUR: Capacité insuffisante");
////                return ResponseEntity.badRequest().body("Capacité insuffisante");
////            }
////
////            // 4. Créer la réservation
////            Reservation savedReservation = reservationRepository.save(reservation);
////            System.out.println("✅ Réservation créée avec succès - ID: " + savedReservation.getId());
////
////            // 5. ✅ NOUVEAU: Mettre à jour la capacité de l'événement
////            try {
////                System.out.println("Mise à jour de la capacité...");
////                eventClient.updateCapacity(reservation.getEventId(), reservation.getNombrePlaces());
////                System.out.println("✅ Capacité mise à jour avec succès");
////            } catch (Exception e) {
////                System.err.println("❌ ERREUR lors de la mise à jour de la capacité: " + e.getMessage());
////                e.printStackTrace();
////
////                // Rollback: supprimer la réservation si la mise à jour échoue
////                reservationRepository.deleteById(savedReservation.getId());
////                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                        .body("Erreur lors de la mise à jour de la capacité de l'événement");
////            }
////
////            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
////
////        } catch (Exception e) {
////            System.err.println("❌ ERREUR GLOBALE: " + e.getMessage());
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
////                    .body("Erreur lors de la communication avec les services: " + e.getMessage());
////        }
////    }
//    @PostMapping
//    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
//        System.out.println("=== CRÉATION RÉSERVATION ===");
//        System.out.println("Event ID: " + reservation.getEventId());
//        System.out.println("User ID: " + reservation.getUserId());
//        System.out.println("Nombre places: " + reservation.getNombrePlaces());
//
//        try {
//            // 1. Vérifier que l'événement existe
//            Event event = eventClient.getEventById(reservation.getEventId());
//            if (event == null) {
//                return ResponseEntity.badRequest().body("Event non trouvé");
//            }
//            System.out.println("✅ Event trouvé: " + event.getTitre());
//
//            // 2. Vérifier que l'utilisateur existe
//            User user = userClient.getUserById(reservation.getUserId());
//            if (user == null) {
//                return ResponseEntity.badRequest().body("User non trouvé");
//            }
//            System.out.println("✅ User trouvé: " + user.getNom());
//
//            // 3. Vérifier la capacité disponible
//            if (event.getCapaciteDisponible() == null ||
//                    event.getCapaciteDisponible() < reservation.getNombrePlaces()) {
//                return ResponseEntity.badRequest().body("Capacité insuffisante");
//            }
//
//            // ✅ 4. Calculer le prix total automatiquement
//            double prixTotal = (event.getPrix() != null ? event.getPrix() : 0.0)
//                    * reservation.getNombrePlaces();
//            reservation.setPrixTotal(prixTotal);
//            System.out.println("✅ Prix total calculé: " + prixTotal + " DH");
//
//            // 5. Sauvegarder la réservation
//            Reservation savedReservation = reservationRepository.save(reservation);
//            System.out.println("✅ Réservation créée - ID: " + savedReservation.getId());
//
//            // 6. Mettre à jour la capacité de l'événement
//            try {
//                eventClient.updateCapacity(reservation.getEventId(), reservation.getNombrePlaces());
//                System.out.println("✅ Capacité mise à jour");
//            } catch (Exception e) {
//                // Rollback
//                reservationRepository.deleteById(savedReservation.getId());
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Erreur mise à jour capacité: " + e.getMessage());
//            }
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
//
//        } catch (Exception e) {
//            System.err.println("❌ ERREUR: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                    .body("Erreur communication services: " + e.getMessage());
//        }
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
//        return reservationRepository.findById(id)
//                .map(existingReservation -> {
//                    existingReservation.setEventId(reservation.getEventId());
//                    existingReservation.setUserId(reservation.getUserId());
//                    existingReservation.setNombrePlaces(reservation.getNombrePlaces());
//                    existingReservation.setStatut(reservation.getStatut());
//                    return ResponseEntity.ok(reservationRepository.save(existingReservation));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}/confirmer")
//    public ResponseEntity<Reservation> confirmerReservation(@PathVariable Long id) {
//        System.out.println("=== CONFIRMATION RÉSERVATION ID: " + id + " ===");
//        return reservationRepository.findById(id)
//                .map(reservation -> {
//                    reservation.setStatut(ReservationStatus.CONFIRMED);
//                    Reservation confirmed = reservationRepository.save(reservation);
//                    System.out.println("✅ Réservation confirmée");
//                    return ResponseEntity.ok(confirmed);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}/annuler")
//    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id) {
//        System.out.println("=== ANNULATION RÉSERVATION ID: " + id + " ===");
//        return reservationRepository.findById(id)
//                .map(reservation -> {
//                    // Sauvegarder les infos avant annulation
//                    int nombrePlaces = reservation.getNombrePlaces();
//                    Long eventId = reservation.getEventId();
//
//                    System.out.println("Nombre de places à restituer: " + nombrePlaces);
//                    System.out.println("Event ID: " + eventId);
//
//                    // Annuler la réservation
//                    reservation.setStatut(ReservationStatus.CANCELLED);
//                    Reservation cancelled = reservationRepository.save(reservation);
//                    System.out.println("✅ Réservation annulée");
//
//                    // ✅ NOUVEAU: Remettre les places disponibles dans l'événement
//                    try {
//                        System.out.println("Restitution des places...");
//                        // Utiliser un nombre négatif pour ajouter des places
//                        eventClient.updateCapacity(eventId, -nombrePlaces);
//                        System.out.println("✅ Places restituées avec succès");
//                    } catch (Exception e) {
//                        System.err.println("❌ ERREUR lors de la restitution des places: " + e.getMessage());
//                        e.printStackTrace();
//                        // On continue quand même, la réservation est annulée
//                    }
//
//                    return ResponseEntity.ok(cancelled);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
//        if (reservationRepository.existsById(id)) {
//            reservationRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//    // Réservations pour les événements d'un organisateur
//    @GetMapping("/organizer/{organizerId}")
//    public ResponseEntity<List<Map<String, Object>>> getReservationsByOrganizer(
//            @PathVariable Long organizerId) {
//
//        // 1. Récupérer les événements de l'organisateur
//        List<Event> myEvents;
//        try {
//            myEvents = eventClient.getEventsByOrganizer(organizerId);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
//        }
//
//        if (myEvents == null || myEvents.isEmpty()) {
//            return ResponseEntity.ok(List.of());
//        }
//
//        // 2. Pour chaque événement, récupérer ses réservations
//        List<Map<String, Object>> allReservations = myEvents.stream()
//                .flatMap(event -> {
//                    List<Reservation> reservations =
//                            reservationRepository.findByEventId(event.getId());
//                    return reservations.stream().map(reservation -> {
//                        Map<String, Object> enriched = new HashMap<>();
//                        enriched.put("id", reservation.getId());
//                        enriched.put("eventId", reservation.getEventId());
//                        enriched.put("userId", reservation.getUserId());
//                        enriched.put("nombrePlaces", reservation.getNombrePlaces());
//                        enriched.put("statut", reservation.getStatut());
//                        enriched.put("dateReservation", reservation.getDateReservation());
//                        enriched.put("prixTotal", reservation.getPrixTotal());
//                        enriched.put("event", event);
//
//                        // Récupérer les infos du client
//                        try {
//                            User user = userClient.getUserById(reservation.getUserId());
//                            enriched.put("user", user);
//                        } catch (Exception e) {
//                            enriched.put("user", null);
//                        }
//                        return enriched;
//                    });
//                })
//                .collect(java.util.stream.Collectors.toList());
//
//        return ResponseEntity.ok(allReservations);
//    }
//}
package com.assoulaimani.reservationservice.controller;

import com.assoulaimani.reservationservice.client.EventClient;
import com.assoulaimani.reservationservice.client.UserClient;
import com.assoulaimani.reservationservice.entity.Reservation;
import com.assoulaimani.reservationservice.entity.ReservationStatus;
import com.assoulaimani.reservationservice.repository.ReservationRepository;
import com.assoulaimani.reservationservice.model.Event;
import com.assoulaimani.reservationservice.model.User;
import com.assoulaimani.reservationservice.security.JwtUtil;
import com.assoulaimani.reservationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventClient eventClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService; // ✅ AJOUTER

    // ========== MÉTHODE UTILITAIRE ==========

    private String extractRole(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractRole(authHeader.substring(7));
            } catch (Exception e) {
                System.err.println("❌ Erreur extraction rôle: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    private Long extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String email = jwtUtil.extractEmail(authHeader.substring(7));
                // Récupérer l'utilisateur par email pour avoir son ID
                User user = userClient.getUserByEmail(email);
                return user != null ? user.getId() : null;
            } catch (Exception e) {
                System.err.println("❌ Erreur extraction userId: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    // ========== ENDPOINTS ADMIN ==========

    /**
     * GET /api/reservations - Toutes les réservations (ADMIN uniquement)
     */
    @GetMapping
    public ResponseEntity<?> getAllReservations(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès réservé aux administrateurs.");
        }

        List<Reservation> reservations = reservationRepository.findAll();

        List<Map<String, Object>> enriched = reservations.stream()
                .map(reservation -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", reservation.getId());
                    map.put("eventId", reservation.getEventId());
                    map.put("userId", reservation.getUserId());
                    map.put("nombrePlaces", reservation.getNombrePlaces());
                    map.put("statut", reservation.getStatut());
                    map.put("dateReservation", reservation.getDateReservation());
                    map.put("prixTotal", reservation.getPrixTotal());

                    try {
                        map.put("event", eventClient.getEventById(reservation.getEventId()));
                    } catch (Exception e) {
                        map.put("event", null);
                    }
                    try {
                        map.put("user", userClient.getUserById(reservation.getUserId()));
                    } catch (Exception e) {
                        map.put("user", null);
                    }
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(enriched);
    }

    // ========== ENDPOINTS USER ==========

    /**
     * GET /api/reservations/user/{userId} - Réservations d'un USER
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReservationsByUser(
            @PathVariable Long userId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token manquant ou invalide.");
        }

        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        List<Map<String, Object>> enrichedReservations = reservations.stream()
                .map(reservation -> {
                    Map<String, Object> enriched = new HashMap<>();
                    enriched.put("id", reservation.getId());
                    enriched.put("eventId", reservation.getEventId());
                    enriched.put("userId", reservation.getUserId());
                    enriched.put("nombrePlaces", reservation.getNombrePlaces());
                    enriched.put("statut", reservation.getStatut());
                    enriched.put("dateReservation", reservation.getDateReservation());
                    enriched.put("prixTotal", reservation.getPrixTotal());

                    try {
                        Event event = eventClient.getEventById(reservation.getEventId());
                        enriched.put("event", event);
                    } catch (Exception e) {
                        System.err.println("Erreur récupération event: " + e.getMessage());
                        enriched.put("event", null);
                    }

                    return enriched;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(enrichedReservations);
    }

    /**
     * POST /api/reservations - Créer une réservation (USER uniquement)
     * Statut CONFIRMED par défaut
     */
    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestBody Reservation reservation,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("=== CRÉATION RÉSERVATION ===");

        // ✅ Vérifier le rôle - Seul USER peut réserver
        String role = extractRole(authHeader);
        if (!"USER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Seuls les utilisateurs peuvent faire des réservations.");
        }

        try {
            // 1. Vérifier que l'événement existe
            Event event = eventClient.getEventById(reservation.getEventId());
            if (event == null) {
                return ResponseEntity.badRequest().body("Événement non trouvé.");
            }
            System.out.println("✅ Event trouvé: " + event.getTitre());

            // 2. Vérifier que l'utilisateur existe
            User user = userClient.getUserById(reservation.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
            }
            System.out.println("✅ User trouvé: " + user.getNom());

            // 3. Vérifier la capacité disponible
            if (event.getCapaciteDisponible() == null ||
                    event.getCapaciteDisponible() < reservation.getNombrePlaces()) {
                return ResponseEntity.badRequest().body("Capacité insuffisante.");
            }

            // 4. Calculer le prix total
            double prixTotal = (event.getPrix() != null ? event.getPrix() : 0.0)
                    * reservation.getNombrePlaces();
            reservation.setPrixTotal(prixTotal);
            System.out.println("✅ Prix total: " + prixTotal + " DH");

            // ✅ 5. Statut CONFIRMED par défaut
            reservation.setStatut(ReservationStatus.CONFIRMED);

            // 6. Sauvegarder
            Reservation savedReservation = reservationRepository.save(reservation);
            System.out.println("✅ Réservation créée - ID: " + savedReservation.getId());

            // 7. Mettre à jour la capacité
            try {
                eventClient.updateCapacity(reservation.getEventId(), reservation.getNombrePlaces());
                System.out.println("✅ Capacité mise à jour");
            } catch (Exception e) {
                reservationRepository.deleteById(savedReservation.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur mise à jour capacité: " + e.getMessage());
            }
            // ✅ 8. ENVOYER NOTIFICATION WEBSOCKET À L'ORGANISATEUR
            try {
                Map<String, Object> notification = new HashMap<>();
                notification.put("type", "NOUVELLE_RESERVATION");
                notification.put("reservationId", savedReservation.getId());
                notification.put("eventId", event.getId());
                notification.put("eventTitre", event.getTitre());
                notification.put("eventLieu", event.getLieu());
                notification.put("clientNom", user.getNom());
                notification.put("clientEmail", user.getEmail());
                notification.put("nombrePlaces", reservation.getNombrePlaces());
                notification.put("prixTotal", prixTotal);
                notification.put("dateReservation", LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                // Envoyer à l'organisateur de l'événement
                notificationService.sendNotificationToOrganizer(
                        event.getOrganisateurId(),
                        notification
                );

            } catch (Exception e) {
                // Ne pas bloquer si notification échoue
                System.err.println("⚠️ Erreur notification: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);

        } catch (Exception e) {
            System.err.println("❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erreur communication services: " + e.getMessage());
        }
    }

    /**
     * PUT /api/reservations/{id}/annuler - Annuler une réservation (USER)
     */
    @PutMapping("/{id}/annuler")
    public ResponseEntity<?> annulerReservation(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("=== ANNULATION RÉSERVATION ID: " + id + " ===");

        String role = extractRole(authHeader);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token manquant ou invalide.");
        }

        return reservationRepository.findById(id)
                .map(reservation -> {
                    if (reservation.getStatut() == ReservationStatus.CANCELLED) {
                        return ResponseEntity.badRequest()
                                .<Reservation>body(null);
                    }

                    int nombrePlaces = reservation.getNombrePlaces();
                    Long eventId = reservation.getEventId();

                    reservation.setStatut(ReservationStatus.CANCELLED);
                    Reservation cancelled = reservationRepository.save(reservation);
                    System.out.println("✅ Réservation annulée");

                    // Restituer les places
                    try {
                        eventClient.updateCapacity(eventId, -nombrePlaces);
                        System.out.println("✅ Places restituées");
                    } catch (Exception e) {
                        System.err.println("❌ Erreur restitution places: " + e.getMessage());
                    }
                    // ✅ Notifier l'organisateur de l'annulation
                    try {
                        Event event = eventClient.getEventById(eventId);
                        User user = userClient.getUserById(reservation.getUserId());

                        if (event != null) {
                            Map<String, Object> notification = new HashMap<>();
                            notification.put("type", "RESERVATION_ANNULEE");
                            notification.put("reservationId", id);
                            notification.put("eventTitre", event.getTitre());
                            notification.put("clientNom",
                                    user != null ? user.getNom() : "Client");
                            notification.put("nombrePlaces", nombrePlaces);
                            notification.put("dateAnnulation", LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

                            notificationService.sendNotificationToOrganizer(
                                    event.getOrganisateurId(),
                                    notification
                            );
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Erreur notification annulation: "
                                + e.getMessage());
                    }

                    return ResponseEntity.ok(cancelled);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/reservations/{id}/confirmer - Confirmer (ADMIN)
     */
    @PutMapping("/{id}/confirmer")
    public ResponseEntity<?> confirmerReservation(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        System.out.println("=== CONFIRMATION RÉSERVATION ID: " + id + " ===");

        String role = extractRole(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès réservé aux administrateurs.");
        }

        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatut(ReservationStatus.CONFIRMED);
                    Reservation confirmed = reservationRepository.save(reservation);
                    System.out.println("✅ Réservation confirmée");
                    return ResponseEntity.ok(confirmed);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    // ========== ENDPOINTS ORGANIZER ==========

    /**
     * GET /api/reservations/organizer/{organizerId} - Réservations de l'organisateur
     */



    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<?> getReservationsByOrganizer(
            @PathVariable Long organizerId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (!"ORGANIZER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès réservé aux organisateurs.");
        }

        // 1. Récupérer les événements de l'organisateur
        List<Event> myEvents;
        try {
            myEvents = eventClient.getEventsByOrganizer(organizerId);
        } catch (Exception e) {
            System.err.println("❌ Erreur récupération events: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erreur communication event-service.");
        }

        if (myEvents == null || myEvents.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // 2. Pour chaque événement, récupérer ses réservations
        List<Map<String, Object>> allReservations = myEvents.stream()
                .flatMap(event -> {
                    List<Reservation> reservations =
                            reservationRepository.findByEventId(event.getId());
                    return reservations.stream().map(reservation -> {
                        Map<String, Object> enriched = new HashMap<>();
                        enriched.put("id", reservation.getId());
                        enriched.put("eventId", reservation.getEventId());
                        enriched.put("userId", reservation.getUserId());
                        enriched.put("nombrePlaces", reservation.getNombrePlaces());
                        enriched.put("statut", reservation.getStatut());
                        enriched.put("dateReservation", reservation.getDateReservation());
                        enriched.put("prixTotal", reservation.getPrixTotal());
                        enriched.put("event", event);

                        try {
                            enriched.put("user", userClient.getUserById(reservation.getUserId()));
                        } catch (Exception e) {
                            enriched.put("user", null);
                        }
                        return enriched;
                    });
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(allReservations);
    }

    // ========== ENDPOINTS COMMUNS ==========

    /**
     * GET /api/reservations/{id} - Détails d'une réservation
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return reservationRepository.findById(id)
                .map(reservation -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", reservation.getId());
                    details.put("eventId", reservation.getEventId());
                    details.put("userId", reservation.getUserId());
                    details.put("nombrePlaces", reservation.getNombrePlaces());
                    details.put("statut", reservation.getStatut());
                    details.put("dateReservation", reservation.getDateReservation());
                    details.put("prixTotal", reservation.getPrixTotal());

                    try {
                        details.put("event", eventClient.getEventById(reservation.getEventId()));
                    } catch (Exception e) {
                        details.put("event", null);
                    }
                    try {
                        details.put("user", userClient.getUserById(reservation.getUserId()));
                    } catch (Exception e) {
                        details.put("user", null);
                    }
                    return ResponseEntity.ok(details);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/reservations/event/{eventId} - Réservations par événement
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getReservationsByEvent(
            @PathVariable Long eventId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(reservationRepository.findByEventId(eventId));
    }

    /**
     * DELETE /api/reservations/{id} - Supprimer (ADMIN)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String role = extractRole(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès réservé aux administrateurs.");
        }

        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}