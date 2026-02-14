package com.assoulaimani.reservationservice.client;


import com.assoulaimani.reservationservice.model.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "event-service", url = "${event.service.url}")
public interface EventClient {

    @GetMapping("/api/events/{id}")
    Event getEventById(@PathVariable("id") Long id);

    @GetMapping("/api/events/{id}/disponibilite")
    Boolean checkDisponibilite(@PathVariable("id") Long id);

    @PutMapping("/api/events/{id}/capacity")
    Event updateCapacity(@PathVariable("id") Long id, @RequestParam("places") int places);
}

