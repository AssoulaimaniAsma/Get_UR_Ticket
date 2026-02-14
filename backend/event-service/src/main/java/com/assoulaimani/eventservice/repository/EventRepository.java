//package com.assoulaimani.eventservice.repository;
//
//
//import com.assoulaimani.eventservice.entity.Event;
//import com.assoulaimani.eventservice.entity.EventStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface EventRepository extends JpaRepository<Event, Long> {
//
//    List<Event> findByTitreContainingIgnoreCase(String titre);
//
//    List<Event> findByCategoryId(Long categoryId);
//
//    List<Event> findByDateEventBetween(LocalDateTime start, LocalDateTime end);
//
//    List<Event> findByLieuContainingIgnoreCase(String lieu);
//
//    List<Event> findByOrganisateurId(Long organisateurId);
//
//    List<Event> findByDateEventAfterOrderByDateEventAsc(LocalDateTime date);
//    // ✅ NOUVEAU: Trouver par statut
//    List<Event> findByStatut(EventStatus statut);
//
//
//
//    // ✅ NOUVEAU: Trouver par organisateur et statut
//    List<Event> findByOrganisateurIdAndStatut(Long organisateurId, EventStatus statut);
//    @Query("SELECT e FROM Event e WHERE e.capaciteDisponible > 0")
//    List<Event> findAvailableEvents();
//}
package com.assoulaimani.eventservice.repository;

import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Recherche par catégorie
    List<Event> findByCategoryId(Long categoryId);

    // Recherche par statut
    List<Event> findByStatut(EventStatus statut);

    // Recherche par organisateur
    List<Event> findByOrganisateurId(Long organisateurId);

    // Recherche par organisateur et statut
    List<Event> findByOrganisateurIdAndStatut(Long organisateurId, EventStatus statut);

    // Recherche par mot-clé dans le titre
    List<Event> findByTitreContainingIgnoreCase(String keyword);

    // Événements à venir (date future)
    List<Event> findByDateEventAfterOrderByDateEventAsc(LocalDateTime date);

    // Événements disponibles (avec places disponibles)
    @Query("SELECT e FROM Event e WHERE e.capaciteDisponible > 0")
    List<Event> findAvailableEvents();
}