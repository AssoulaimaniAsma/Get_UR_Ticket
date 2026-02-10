package com.assoulaimani.eventservice.controller;


import com.assoulaimani.eventservice.entity.Category;
import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        event.setCapaciteDisponible(event.getCapaciteTotal());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        if (event.getCategory() != null && event.getCategory().getId() != null) {
            Category category = categoryRepository.findById(event.getCategory().getId())
                    .orElse(null);
            event.setCategory(category);
        }

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/capacity")
    public ResponseEntity<Event> updateCapacity(@PathVariable Long id, @RequestParam int places) {
        return eventRepository.findById(id)
                .map(event -> {
                    int newCapacity = event.getCapaciteDisponible() - places;
                    if (newCapacity < 0) {
                        return ResponseEntity.badRequest().<Event>build();
                    }
                    event.setCapaciteDisponible(newCapacity);
                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String keyword) {
        List<Event> events = eventRepository.findByTitreContainingIgnoreCase(keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable Long categoryId) {
        List<Event> events = eventRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventRepository.findByDateEventAfterOrderByDateEventAsc(LocalDateTime.now());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Event>> getAvailableEvents() {
        List<Event> events = eventRepository.findAvailableEvents();
        return ResponseEntity.ok(events);
    }
}