package com.assoulaimani.eventservice.repository;


import com.assoulaimani.eventservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitreContainingIgnoreCase(String titre);

    List<Event> findByCategoryId(Long categoryId);

    List<Event> findByDateEventBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findByLieuContainingIgnoreCase(String lieu);

    List<Event> findByOrganisateurId(Long organisateurId);

    List<Event> findByDateEventAfterOrderByDateEventAsc(LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE e.capaciteDisponible > 0")
    List<Event> findAvailableEvents();
}