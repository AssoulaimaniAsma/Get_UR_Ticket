package com.assoulaimani.reservationservice.controller;


import com.assoulaimani.reservationservice.client.EventClient;
import com.assoulaimani.reservationservice.client.UserClient;
import com.assoulaimani.reservationservice.entity.Reservation;
import com.assoulaimani.reservationservice.entity.ReservationStatus;
import com.assoulaimani.reservationservice.repository.ReservationRepository;
import com.assoulaimani.reservationservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
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

    // Vérifier la disponibilité avant de créer une réservation
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            // Vérifier que l'événement existe
            var event = eventClient.getEventById(reservation.getEventId());
            if (event == null) {
                return ResponseEntity.badRequest().body("Event non trouvé");
            }

            // Vérifier que l'utilisateur existe
            var user = userClient.getUserById(reservation.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("User non trouvé");
            }

            // Vérifier la disponibilité
            Integer placesReservees = reservationRepository.countPlacesReserveesByEventId(reservation.getEventId());
            placesReservees = placesReservees != null ? placesReservees : 0;

            if (event.getCapaciteTotal() != null &&
                    (placesReservees + reservation.getNombrePlaces()) > event.getCapaciteTotal()) {
                return ResponseEntity.badRequest().body("Capacité insuffisante");
            }

            Reservation savedReservation = reservationRepository.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erreur lors de la communication avec les services");
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
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatut(ReservationStatus.CONFIRMED);
                    return ResponseEntity.ok(reservationRepository.save(reservation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Reservation> annulerReservation(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatut(ReservationStatus.CANCELLED);
                    return ResponseEntity.ok(reservationRepository.save(reservation));
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