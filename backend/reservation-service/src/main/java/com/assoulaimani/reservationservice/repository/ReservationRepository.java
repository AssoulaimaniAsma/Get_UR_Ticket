package com.assoulaimani.reservationservice.repository;


import com.assoulaimani.reservationservice.entity.Reservation;
import com.assoulaimani.reservationservice.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByEventId(Long eventId);

    List<Reservation> findByStatut(ReservationStatus statut);

    List<Reservation> findByEventIdAndStatut(Long eventId, ReservationStatus statut);

    List<Reservation> findByUserIdAndStatut(Long userId, ReservationStatus statut);

    @Query("SELECT SUM(r.nombrePlaces) FROM Reservation r WHERE r.eventId = :eventId AND r.statut != 'CANCELLED'")
    Integer countPlacesReserveesByEventId(Long eventId);
}