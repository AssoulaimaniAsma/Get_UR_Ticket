package com.assoulaimani.reservationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "reservations")

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "nombre_places", nullable = false)
    private Integer nombrePlaces;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus statut;
}
