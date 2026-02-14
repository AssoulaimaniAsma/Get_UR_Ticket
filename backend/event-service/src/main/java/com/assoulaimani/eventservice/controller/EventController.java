//package com.assoulaimani.eventservice.controller;
//
//
//import com.assoulaimani.eventservice.entity.Category;
//import com.assoulaimani.eventservice.entity.Event;
//import com.assoulaimani.eventservice.entity.EventStatus;
//import com.assoulaimani.eventservice.repository.CategoryRepository;
//import com.assoulaimani.eventservice.repository.EventRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/events")
//@RequiredArgsConstructor
////@CrossOrigin(origins = "http://localhost:3000")
////public class EventController {
////
////    private final EventRepository eventRepository;
////    private final CategoryRepository categoryRepository;
////
////    @GetMapping
////    public ResponseEntity<List<Event>> getAllEvents() {
////        return ResponseEntity.ok(eventRepository.findAll());
////    }
////
////    @GetMapping("/{id}")
////    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
////        return eventRepository.findById(id)
////                .map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @PostMapping
////    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
////        event.setCapaciteDisponible(event.getCapaciteTotal());
////        event.setCreatedAt(LocalDateTime.now());
////        event.setUpdatedAt(LocalDateTime.now());
////
////        if (event.getCategory() != null && event.getCategory().getId() != null) {
////            Category category = categoryRepository.findById(event.getCategory().getId())
////                    .orElse(null);
////            event.setCategory(category);
////        }
////
////        Event savedEvent = eventRepository.save(event);
////        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
////    }
////
////    @PutMapping("/{id}")
////    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
////        return eventRepository.findById(id)
////                .map(event -> {
////                    event.setTitre(eventDetails.getTitre());
////                    event.setDescription(eventDetails.getDescription());
////                    event.setDateEvent(eventDetails.getDateEvent());
////                    event.setLieu(eventDetails.getLieu());
////                    event.setCapaciteTotal(eventDetails.getCapaciteTotal());
////                    event.setPrix(eventDetails.getPrix());
////                    event.setImageUrl(eventDetails.getImageUrl());
////                    event.setUpdatedAt(LocalDateTime.now());
////
////                    if (eventDetails.getCategory() != null && eventDetails.getCategory().getId() != null) {
////                        Category category = categoryRepository.findById(eventDetails.getCategory().getId())
////                                .orElse(null);
////                        event.setCategory(category);
////                    }
////
////                    Event updatedEvent = eventRepository.save(event);
////                    return ResponseEntity.ok(updatedEvent);
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
////        if (!eventRepository.existsById(id)) {
////            return ResponseEntity.notFound().build();
////        }
////        eventRepository.deleteById(id);
////        return ResponseEntity.noContent().build();
////    }
////
////    @PutMapping("/{id}/capacity")
////    public ResponseEntity<Event> updateCapacity(@PathVariable Long id, @RequestParam int places) {
////        return eventRepository.findById(id)
////                .map(event -> {
////                    int newCapacity = event.getCapaciteDisponible() - places;
////                    if (newCapacity < 0) {
////                        return ResponseEntity.badRequest().<Event>build();
////                    }
////                    event.setCapaciteDisponible(newCapacity);
////                    Event updatedEvent = eventRepository.save(event);
////                    return ResponseEntity.ok(updatedEvent);
////                })
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @GetMapping("/search")
////    public ResponseEntity<List<Event>> searchEvents(@RequestParam String keyword) {
////        List<Event> events = eventRepository.findByTitreContainingIgnoreCase(keyword);
////        return ResponseEntity.ok(events);
////    }
////
////    @GetMapping("/category/{categoryId}")
////    public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable Long categoryId) {
////        List<Event> events = eventRepository.findByCategoryId(categoryId);
////        return ResponseEntity.ok(events);
////    }
////
////    @GetMapping("/upcoming")
////    public ResponseEntity<List<Event>> getUpcomingEvents() {
////        List<Event> events = eventRepository.findByDateEventAfterOrderByDateEventAsc(LocalDateTime.now());
////        return ResponseEntity.ok(events);
////    }
////
////    @GetMapping("/available")
////    public ResponseEntity<List<Event>> getAvailableEvents() {
////        List<Event> events = eventRepository.findAvailableEvents();
////        return ResponseEntity.ok(events);
////    }
////}
//public class EventController {
//
//    @Autowired
//    private EventRepository eventRepository;
//
//    // ✅ Récupérer uniquement les événements approuvés (pour le public)
//    @GetMapping
//    public ResponseEntity<List<Event>> getAllApprovedEvents() {
//        List<Event> events = eventRepository.findByStatut(EventStatus.APPROVED);
//        return ResponseEntity.ok(events);
//    }
//
//    // ✅ NOUVEAU: Récupérer tous les événements (ADMIN)
//    @GetMapping("/admin/all")
//    public ResponseEntity<List<Event>> getAllEvents() {
//        return ResponseEntity.ok(eventRepository.findAll());
//    }
//
//    // ✅ NOUVEAU: Récupérer les événements en attente (ADMIN)
//    @GetMapping("/admin/pending")
//    public ResponseEntity<List<Event>> getPendingEvents() {
//        List<Event> events = eventRepository.findByStatut(EventStatus.PENDING);
//        return ResponseEntity.ok(events);
//    }
//
//    // ✅ NOUVEAU: Récupérer mes événements (ORGANIZER)
//    @GetMapping("/organizer/{organizerId}")
//    public ResponseEntity<List<Event>> getMyEvents(@PathVariable Long organizerId) {
//        List<Event> events = eventRepository.findByOrganisateurId(organizerId);
//        return ResponseEntity.ok(events);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
//        return eventRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable Long categoryId) {
//        List<Event> events = eventRepository.findByCategoryId(categoryId);
//        // Filtrer uniquement les événements approuvés
//        events = events.stream()
//                .filter(e -> e.getStatut() == EventStatus.APPROVED)
//                .toList();
//        return ResponseEntity.ok(events);
//    }
//
//    // ✅ NOUVEAU: Créer un événement (ORGANIZER) - statut PENDING par défaut
//    @PostMapping("/organizer/create")
//    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
//        event.setStatut(EventStatus.PENDING);
//        event.setCapaciteDisponible(event.getCapaciteTotal());
//        Event savedEvent = eventRepository.save(event);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
//    }
//
//    // ✅ NOUVEAU: Approuver un événement (ADMIN)
//    @PutMapping("/admin/{id}/approve")
//    public ResponseEntity<Event> approveEvent(@PathVariable Long id) {
//        return eventRepository.findById(id)
//                .map(event -> {
//                    event.setStatut(EventStatus.APPROVED);
//                    return ResponseEntity.ok(eventRepository.save(event));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // ✅ NOUVEAU: Rejeter un événement (ADMIN)
//    @PutMapping("/admin/{id}/reject")
//    public ResponseEntity<Event> rejectEvent(@PathVariable Long id) {
//        return eventRepository.findById(id)
//                .map(event -> {
//                    event.setStatut(EventStatus.REJECTED);
//                    return ResponseEntity.ok(eventRepository.save(event));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
//        return eventRepository.findById(id)
//                .map(existingEvent -> {
//                    existingEvent.setTitre(event.getTitre());
//                    existingEvent.setDescription(event.getDescription());
//                    existingEvent.setDateEvent(event.getDateEvent());
//                    existingEvent.setLieu(event.getLieu());
//                    existingEvent.setCapaciteTotal(event.getCapaciteTotal());
//                    existingEvent.setPrix(event.getPrix());
//                    existingEvent.setCategory(event.getCategory());
//                    existingEvent.setImageUrl(event.getImageUrl());
//                    return ResponseEntity.ok(eventRepository.save(existingEvent));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
//        if (eventRepository.existsById(id)) {
//            eventRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}
package com.assoulaimani.eventservice.controller;

import com.assoulaimani.eventservice.entity.Category;
import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.entity.EventStatus;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    // ========== PUBLIC ENDPOINTS (Tous les utilisateurs) ==========

    /**
     * Récupérer uniquement les événements APPROUVÉS et disponibles
     */
    @GetMapping
    public ResponseEntity<List<Event>> getAllApprovedEvents() {
        List<Event> events = eventRepository.findByStatut(EventStatus.APPROVED);
        return ResponseEntity.ok(events);
    }

    /**
     * Récupérer un événement par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupérer les événements par catégorie (uniquement APPROUVÉS)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable Long categoryId) {
        List<Event> events = eventRepository.findByCategoryId(categoryId);
        // Filtrer uniquement les événements approuvés
        events = events.stream()
                .filter(e -> e.getStatut() == EventStatus.APPROVED)
                .toList();
        return ResponseEntity.ok(events);
    }

    /**
     * Rechercher des événements par mot-clé (uniquement APPROUVÉS)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String keyword) {
        List<Event> events = eventRepository.findByTitreContainingIgnoreCase(keyword);
        // Filtrer uniquement les événements approuvés
        events = events.stream()
                .filter(e -> e.getStatut() == EventStatus.APPROVED)
                .toList();
        return ResponseEntity.ok(events);
    }

    /**
     * Récupérer les événements à venir (uniquement APPROUVÉS)
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventRepository.findByDateEventAfterOrderByDateEventAsc(LocalDateTime.now());
        // Filtrer uniquement les événements approuvés
        events = events.stream()
                .filter(e -> e.getStatut() == EventStatus.APPROVED)
                .toList();
        return ResponseEntity.ok(events);
    }

    /**
     * Récupérer les événements disponibles (uniquement APPROUVÉS)
     */
    @GetMapping("/available")
    public ResponseEntity<List<Event>> getAvailableEvents() {
        List<Event> events = eventRepository.findAvailableEvents();
        // Filtrer uniquement les événements approuvés
        events = events.stream()
                .filter(e -> e.getStatut() == EventStatus.APPROVED)
                .toList();
        return ResponseEntity.ok(events);
    }

    // ========== ORGANIZER ENDPOINTS ==========

    /**
     * Créer un événement (ORGANIZER)
     * Statut PENDING par défaut
     */
    @PostMapping("/organizer/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        System.out.println("=== CRÉATION ÉVÉNEMENT ===");
        System.out.println("Titre: " + event.getTitre());
        System.out.println("Organisateur ID: " + event.getOrganisateurId());

        // Définir les valeurs par défaut
        event.setStatut(EventStatus.PENDING);
        event.setCapaciteDisponible(event.getCapaciteTotal());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        // Associer la catégorie
        if (event.getCategory() != null && event.getCategory().getId() != null) {
            Category category = categoryRepository.findById(event.getCategory().getId())
                    .orElse(null);
            event.setCategory(category);
        }

        Event savedEvent = eventRepository.save(event);
        System.out.println("Événement créé avec ID: " + savedEvent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    /**
     * Récupérer mes événements (ORGANIZER)
     */
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> getMyEvents(@PathVariable Long organizerId) {
        List<Event> events = eventRepository.findByOrganisateurId(organizerId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache().noStore())
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(events);
    }

    /**
     * Modifier un événement (ORGANIZER)
     * Seul l'organisateur propriétaire peut modifier
     */
    @PutMapping("/organizer/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    // Vérifier que l'organisateur est le propriétaire
                    if (!event.getOrganisateurId().equals(eventDetails.getOrganisateurId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Event>build();
                    }

                    event.setTitre(eventDetails.getTitre());
                    event.setDescription(eventDetails.getDescription());
                    event.setDateEvent(eventDetails.getDateEvent());
                    event.setLieu(eventDetails.getLieu());
                    event.setCapaciteTotal(eventDetails.getCapaciteTotal());
                    event.setPrix(eventDetails.getPrix());
                    event.setImageUrl(eventDetails.getImageUrl());
                    event.setUpdatedAt(LocalDateTime.now());

                    if (eventDetails.getCategory() != null && eventDetails.getCategory().getId() != null) {
                        Category category = categoryRepository.findById(eventDetails.getCategory().getId())
                                .orElse(null);
                        event.setCategory(category);
                    }

                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprimer un événement (ORGANIZER)
     */
    @DeleteMapping("/organizer/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ADMIN ENDPOINTS ==========

    /**
     * Récupérer tous les événements (ADMIN)
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    /**
     * Récupérer les événements en attente (ADMIN)
     */
    @GetMapping("/admin/pending")
    public ResponseEntity<List<Event>> getPendingEvents() {
        List<Event> events = eventRepository.findByStatut(EventStatus.PENDING);
        return ResponseEntity.ok(events);
    }

    /**
     * Approuver un événement (ADMIN)
     */
    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<Event> approveEvent(@PathVariable Long id) {
        System.out.println("=== APPROBATION ÉVÉNEMENT ID: " + id + " ===");
        return eventRepository.findById(id)
                .map(event -> {
                    event.setStatut(EventStatus.APPROVED);
                    event.setUpdatedAt(LocalDateTime.now());
                    Event approvedEvent = eventRepository.save(event);
                    System.out.println("Événement approuvé: " + approvedEvent.getTitre());
                    return ResponseEntity.ok(approvedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Rejeter un événement (ADMIN)
     */
    @PutMapping("/admin/{id}/reject")
    public ResponseEntity<Event> rejectEvent(@PathVariable Long id) {
        System.out.println("=== REJET ÉVÉNEMENT ID: " + id + " ===");
        return eventRepository.findById(id)
                .map(event -> {
                    event.setStatut(EventStatus.REJECTED);
                    event.setUpdatedAt(LocalDateTime.now());
                    Event rejectedEvent = eventRepository.save(event);
                    System.out.println("Événement rejeté: " + rejectedEvent.getTitre());
                    return ResponseEntity.ok(rejectedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Modifier n'importe quel événement (ADMIN)
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<Event> adminUpdateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitre(eventDetails.getTitre());
                    event.setDescription(eventDetails.getDescription());
                    event.setDateEvent(eventDetails.getDateEvent());
                    event.setLieu(eventDetails.getLieu());
                    event.setCapaciteTotal(eventDetails.getCapaciteTotal());
                    event.setCapaciteDisponible(eventDetails.getCapaciteDisponible());
                    event.setPrix(eventDetails.getPrix());
                    event.setImageUrl(eventDetails.getImageUrl());
                    event.setStatut(eventDetails.getStatut());
                    event.setUpdatedAt(LocalDateTime.now());

                    if (eventDetails.getCategory() != null && eventDetails.getCategory().getId() != null) {
                        Category category = categoryRepository.findById(eventDetails.getCategory().getId())
                                .orElse(null);
                        event.setCategory(category);
                    }

                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprimer n'importe quel événement (ADMIN)
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========== INTERNAL ENDPOINTS (Pour les autres microservices) ==========

    /**
     * Mettre à jour la capacité disponible
     * Utilisé par le Reservation Service lors d'une réservation
     */
    @PutMapping("/{id}/capacity")
    public ResponseEntity<Event> updateCapacity(@PathVariable Long id, @RequestParam int places) {
        System.out.println("=== MISE À JOUR CAPACITÉ ===");
        System.out.println("Event ID: " + id + ", Places à réserver: " + places);

        return eventRepository.findById(id)
                .map(event -> {
                    int newCapacity = event.getCapaciteDisponible() - places;
                    System.out.println("Capacité actuelle: " + event.getCapaciteDisponible());
                    System.out.println("Nouvelle capacité: " + newCapacity);

                    if (newCapacity < 0) {
                        System.out.println("ERREUR: Capacité insuffisante");
                        return ResponseEntity.badRequest().<Event>build();
                    }

                    event.setCapaciteDisponible(newCapacity);
                    event.setUpdatedAt(LocalDateTime.now());
                    Event updatedEvent = eventRepository.save(event);
                    System.out.println("Capacité mise à jour avec succès");
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Vérifier la disponibilité d'un événement
     */
    @GetMapping("/{id}/disponibilite")
    public ResponseEntity<Boolean> checkDisponibilite(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    boolean disponible = event.getCapaciteDisponible() > 0
                            && event.getStatut() == EventStatus.APPROVED;
                    return ResponseEntity.ok(disponible);
                })
                .orElse(ResponseEntity.ok(false));
    }
}