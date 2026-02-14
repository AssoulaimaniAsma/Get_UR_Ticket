//package com.assoulaimani.reservationservice.controller;
//
//
//import com.assoulaimani.reservationservice.client.EventClient;
//import com.assoulaimani.reservationservice.client.UserClient;
//import com.assoulaimani.reservationservice.entity.Reservation;
//import com.assoulaimani.reservationservice.entity.ReservationStatus;
//import com.assoulaimani.reservationservice.repository.ReservationRepository;
//import com.assoulaimani.reservationservice.model.*;
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
//                        details.put("event", null);
//                    }
//
//                    try {
//                        details.put("user", userClient.getUserById(reservation.getUserId()));
//                    } catch (Exception e) {
//                        details.put("user", null);
//                    }
//
//                    return ResponseEntity.ok(details);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(reservationRepository.findByUserId(userId));
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
//    // Vérifier la disponibilité avant de créer une réservation
//    @PostMapping
//    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
//        try {
//            // Vérifier que l'événement existe
//            var event = eventClient.getEventById(reservation.getEventId());
//            if (event == null) {
//                return ResponseEntity.badRequest().body("Event non trouvé");
//            }
//
//            // Vérifier que l'utilisateur existe
//            var user = userClient.getUserById(reservation.getUserId());
//            if (user == null) {
//                return ResponseEntity.badRequest().body("User non trouvé");
//            }
//
//            // Vérifier la disponibilité
//            Integer placesReservees = reservationRepository.countPlacesReserveesByEventId(reservation.getEventId());
//            placesReservees = placesReservees != null ? placesReservees : 0;
//
//            if (event.getCapaciteTotal() != null &&
//                    (placesReservees + reservation.getNombrePlaces()) > event.getCapaciteTotal()) {
//                return ResponseEntity.badRequest().body("Capacité insuffisante");
//            }
//
//            Reservation savedReservation = reservationRepository.save(reservation);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                    .body("Erreur lors de la communication avec les services");
//        }
//    }
//
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
//        return reservationRepository.findById(id)
//                .map(reservation -> {
//                    reservation.setStatut(ReservationStatus.CONFIRMED);
//                    return ResponseEntity.ok(reservationRepository.save(reservation));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}/annuler")
//    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id) {
//        return reservationRepository.findById(id)
//                .map(reservation -> {
//                    reservation.setStatut(ReservationStatus.CANCELLED);
//                    return ResponseEntity.ok(reservationRepository.save(reservation));
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
//}
package com.assoulaimani.reservationservice.controller;

import com.assoulaimani.reservationservice.client.EventClient;
import com.assoulaimani.reservationservice.client.UserClient;
import com.assoulaimani.reservationservice.entity.Reservation;
import com.assoulaimani.reservationservice.entity.ReservationStatus;
import com.assoulaimani.reservationservice.repository.ReservationRepository;
import com.assoulaimani.reservationservice.model.Event;
import com.assoulaimani.reservationservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
//@CrossOrigin(origins = "http://localhost:3000") // ✅ AJOUT CORS
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventClient eventClient;

    @Autowired
    private UserClient userClient;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer une réservation avec détails Event et User
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getReservationDetails(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("reservation", reservation);

                    try {
                        details.put("event", eventClient.getEventById(reservation.getEventId()));
                    } catch (Exception e) {
                        System.err.println("Erreur récupération event: " + e.getMessage());
                        details.put("event", null);
                    }

                    try {
                        details.put("user", userClient.getUserById(reservation.getUserId()));
                    } catch (Exception e) {
                        System.err.println("Erreur récupération user: " + e.getMessage());
                        details.put("user", null);
                    }

                    return ResponseEntity.ok(details);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationRepository.findByUserId(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Reservation>> getReservationsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(reservationRepository.findByEventId(eventId));
    }

    @GetMapping("/event/{eventId}/places-reservees")
    public ResponseEntity<Integer> getPlacesReserveesByEvent(@PathVariable Long eventId) {
        Integer places = reservationRepository.countPlacesReserveesByEventId(eventId);
        return ResponseEntity.ok(places != null ? places : 0);
    }

    // ✅ CORRIGÉ: Vérifier la disponibilité avant de créer une réservation
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        System.out.println("=== CRÉATION RÉSERVATION ===");
        System.out.println("Event ID: " + reservation.getEventId());
        System.out.println("User ID: " + reservation.getUserId());
        System.out.println("Nombre places: " + reservation.getNombrePlaces());
        System.out.println("Statut: " + reservation.getStatut());

        try {
            // 1. Vérifier que l'événement existe
            Event event = eventClient.getEventById(reservation.getEventId());
            if (event == null) {
                System.err.println("ERREUR: Event non trouvé");
                return ResponseEntity.badRequest().body("Event non trouvé");
            }
            System.out.println("Event trouvé: " + event.getTitre());

            // 2. Vérifier que l'utilisateur existe
            User user = userClient.getUserById(reservation.getUserId());
            if (user == null) {
                System.err.println("ERREUR: User non trouvé");
                return ResponseEntity.badRequest().body("User non trouvé");
            }
            System.out.println("User trouvé: " + user.getNom());

            // 3. Vérifier la capacité disponible
            System.out.println("Capacité disponible de l'event: " + event.getCapaciteDisponible());
            System.out.println("Places demandées: " + reservation.getNombrePlaces());

            if (event.getCapaciteDisponible() == null ||
                    event.getCapaciteDisponible() < reservation.getNombrePlaces()) {
                System.err.println("ERREUR: Capacité insuffisante");
                return ResponseEntity.badRequest().body("Capacité insuffisante");
            }

            // 4. Créer la réservation
            Reservation savedReservation = reservationRepository.save(reservation);
            System.out.println("✅ Réservation créée avec succès - ID: " + savedReservation.getId());

            // 5. ✅ NOUVEAU: Mettre à jour la capacité de l'événement
            try {
                System.out.println("Mise à jour de la capacité...");
                eventClient.updateCapacity(reservation.getEventId(), reservation.getNombrePlaces());
                System.out.println("✅ Capacité mise à jour avec succès");
            } catch (Exception e) {
                System.err.println("❌ ERREUR lors de la mise à jour de la capacité: " + e.getMessage());
                e.printStackTrace();

                // Rollback: supprimer la réservation si la mise à jour échoue
                reservationRepository.deleteById(savedReservation.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de la mise à jour de la capacité de l'événement");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);

        } catch (Exception e) {
            System.err.println("❌ ERREUR GLOBALE: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erreur lors de la communication avec les services: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    existingReservation.setEventId(reservation.getEventId());
                    existingReservation.setUserId(reservation.getUserId());
                    existingReservation.setNombrePlaces(reservation.getNombrePlaces());
                    existingReservation.setStatut(reservation.getStatut());
                    return ResponseEntity.ok(reservationRepository.save(existingReservation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/confirmer")
    public ResponseEntity<Reservation> confirmerReservation(@PathVariable Long id) {
        System.out.println("=== CONFIRMATION RÉSERVATION ID: " + id + " ===");
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatut(ReservationStatus.CONFIRMED);
                    Reservation confirmed = reservationRepository.save(reservation);
                    System.out.println("✅ Réservation confirmée");
                    return ResponseEntity.ok(confirmed);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id) {
        System.out.println("=== ANNULATION RÉSERVATION ID: " + id + " ===");
        return reservationRepository.findById(id)
                .map(reservation -> {
                    // Sauvegarder les infos avant annulation
                    int nombrePlaces = reservation.getNombrePlaces();
                    Long eventId = reservation.getEventId();

                    System.out.println("Nombre de places à restituer: " + nombrePlaces);
                    System.out.println("Event ID: " + eventId);

                    // Annuler la réservation
                    reservation.setStatut(ReservationStatus.CANCELLED);
                    Reservation cancelled = reservationRepository.save(reservation);
                    System.out.println("✅ Réservation annulée");

                    // ✅ NOUVEAU: Remettre les places disponibles dans l'événement
                    try {
                        System.out.println("Restitution des places...");
                        // Utiliser un nombre négatif pour ajouter des places
                        eventClient.updateCapacity(eventId, -nombrePlaces);
                        System.out.println("✅ Places restituées avec succès");
                    } catch (Exception e) {
                        System.err.println("❌ ERREUR lors de la restitution des places: " + e.getMessage());
                        e.printStackTrace();
                        // On continue quand même, la réservation est annulée
                    }

                    return ResponseEntity.ok(cancelled);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}