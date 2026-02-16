package com.assoulaimani.reservationservice.client;


import com.assoulaimani.reservationservice.config.FeignConfig;
import com.assoulaimani.reservationservice.model.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "event-service", url = "${event.service.url}",configuration = FeignConfig.class)
public interface EventClient {

    @GetMapping("/api/events/{id}")
    Event getEventById(@PathVariable("id") Long id);

    @GetMapping("/api/events/{id}/disponibilite")
    Boolean checkDisponibilite(@PathVariable("id") Long id);

    @PutMapping("/api/events/{id}/capacity")
    Event updateCapacity(@PathVariable("id") Long id, @RequestParam("places") int places);
    // EventClient.java - Ajouter cette méthode
    @GetMapping("/api/events/organizer/{organizerId}")
    List<Event> getEventsByOrganizer(@PathVariable("organizerId") Long organizerId);
}

