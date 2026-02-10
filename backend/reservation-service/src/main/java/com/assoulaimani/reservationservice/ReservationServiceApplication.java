package com.assoulaimani.reservationservice;

import com.assoulaimani.reservationservice.entity.Reservation;
import com.assoulaimani.reservationservice.entity.ReservationStatus;
import com.assoulaimani.reservationservice.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner loadReservationData(ReservationRepository reservationRepository) {
        return args -> {
            System.out.println("========== Chargement des données de test (RESERVATION SERVICE) ==========");

            // Réservation 1: User 1 réserve pour Concert de Jazz (Event 1)
            Reservation reservation1 = new Reservation();
            reservation1.setEventId(1L);
            reservation1.setUserId(5L); // Youssef
            reservation1.setNombrePlaces(2);
            reservation1.setDateReservation(LocalDateTime.now().minusDays(5));
            reservation1.setStatut(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation1);

            // Réservation 2: User 2 réserve pour Match Raja vs WAC (Event 2)
            Reservation reservation2 = new Reservation();
            reservation2.setEventId(2L);
            reservation2.setUserId(6L); // Salma
            reservation2.setNombrePlaces(4);
            reservation2.setDateReservation(LocalDateTime.now().minusDays(3));
            reservation2.setStatut(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation2);

            // Réservation 3: User 3 réserve pour Conférence Tech (Event 3)
            Reservation reservation3 = new Reservation();
            reservation3.setEventId(3L);
            reservation3.setUserId(7L); // Karim
            reservation3.setNombrePlaces(1);
            reservation3.setDateReservation(LocalDateTime.now().minusDays(2));
            reservation3.setStatut(ReservationStatus.PENDING);
            reservationRepository.save(reservation3);

            // Réservation 4: User 1 réserve pour Festival Gnaoua (Event 4)
            Reservation reservation4 = new Reservation();
            reservation4.setEventId(4L);
            reservation4.setUserId(5L); // Youssef
            reservation4.setNombrePlaces(3);
            reservation4.setDateReservation(LocalDateTime.now().minusDays(1));
            reservation4.setStatut(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation4);

            // Réservation 5: User 2 réserve pour Marathon (Event 5) - ANNULÉE
            Reservation reservation5 = new Reservation();
            reservation5.setEventId(5L);
            reservation5.setUserId(6L); // Salma
            reservation5.setNombrePlaces(1);
            reservation5.setDateReservation(LocalDateTime.now().minusDays(7));
            reservation5.setStatut(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation5);

            // Réservation 6: User 3 réserve pour Concert de Jazz (Event 1)
            Reservation reservation6 = new Reservation();
            reservation6.setEventId(1L);
            reservation6.setUserId(7L); // Karim
            reservation6.setNombrePlaces(2);
            reservation6.setDateReservation(LocalDateTime.now());
            reservation6.setStatut(ReservationStatus.PENDING);
            reservationRepository.save(reservation6);

            // Afficher les données
            System.out.println("\n📋 RESERVATIONS:");
            List<Reservation> reservations = reservationRepository.findAll();
            reservations.forEach(reservation -> System.out.println(
                    "  - [ID: " + reservation.getId() + "] " +
                            "Event: " + reservation.getEventId() +
                            " | User: " + reservation.getUserId() +
                            " | Places: " + reservation.getNombrePlaces() +
                            " | Statut: " + reservation.getStatut() +
                            " | Date: " + reservation.getDateReservation()
            ));

            System.out.println("\n📊 Statistiques:");
            System.out.println("  Total réservations: " + reservations.size());
            System.out.println("  CONFIRMED: " + reservations.stream().filter(r -> r.getStatut() == ReservationStatus.CONFIRMED).count());
            System.out.println("  PENDING: " + reservations.stream().filter(r -> r.getStatut() == ReservationStatus.PENDING).count());
            System.out.println("  CANCELLED: " + reservations.stream().filter(r -> r.getStatut() == ReservationStatus.CANCELLED).count());

            System.out.println("\n========== Données chargées avec succès ! ==========\n");
        };
    }
}