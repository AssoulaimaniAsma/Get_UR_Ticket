package com.assoulaimani.eventservice;

import com.assoulaimani.eventservice.entity.Category;
import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
@EnableDiscoveryClient

public class EventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner loadEventData(CategoryRepository categoryRepository,
                                           EventRepository eventRepository) {
        return args -> {
            System.out.println("========== Chargement des données de test (EVENT SERVICE) ==========");

            // Créer des catégories
            Category music = Category.builder()
                    .nom("Musique")
                    .description("Concerts et événements musicaux")
                    .iconUrl("🎵")
                    .build();
            categoryRepository.save(music);

            Category sport = Category.builder()
                    .nom("Sport")
                    .description("Événements sportifs")
                    .iconUrl("⚽")
                    .build();
            categoryRepository.save(sport);

            Category conference = Category.builder()
                    .nom("Conférence")
                    .description("Conférences et séminaires")
                    .iconUrl("📊")
                    .build();
            categoryRepository.save(conference);

            Category culture = Category.builder()
                    .nom("Culture")
                    .description("Événements culturels")
                    .iconUrl("🎭")
                    .build();
            categoryRepository.save(culture);

            // Créer des événements
            Event event1 = new Event();
            event1.setTitre("Concert de Jazz");
            event1.setDescription("Soirée jazz avec des artistes internationaux");
            event1.setDateEvent(LocalDateTime.of(2026, 3, 15, 20, 0));
            event1.setLieu("Théâtre Mohammed V, Rabat");
            event1.setCapaciteTotal(500);
            event1.setCapaciteDisponible(500);
            event1.setPrix(250.0);
            event1.setCategory(music);
            event1.setOrganisateurId(1L);
            event1.setImageUrl("https://example.com/jazz-concert.jpg");
            eventRepository.save(event1);

            Event event2 = new Event();
            event2.setTitre("Match Raja vs WAC");
            event2.setDescription("Derby de Casablanca - Botola Pro");
            event2.setDateEvent(LocalDateTime.of(2026, 3, 20, 19, 0));
            event2.setLieu("Stade Mohammed V, Casablanca");
            event2.setCapaciteTotal(45000);
            event2.setCapaciteDisponible(45000);
            event2.setPrix(150.0);
            event2.setCategory(sport);
            event2.setOrganisateurId(2L);
            event2.setImageUrl("https://example.com/derby.jpg");
            eventRepository.save(event2);

            Event event3 = new Event();
            event3.setTitre("Conférence Tech Morocco");
            event3.setDescription("Innovation et technologies au Maroc");
            event3.setDateEvent(LocalDateTime.of(2026, 4, 10, 9, 0));
            event3.setLieu("Technopark, Casablanca");
            event3.setCapaciteTotal(200);
            event3.setCapaciteDisponible(200);
            event3.setPrix(0.0);
            event3.setCategory(conference);
            event3.setOrganisateurId(1L);
            event3.setImageUrl("https://example.com/tech-conference.jpg");
            eventRepository.save(event3);

            Event event4 = new Event();
            event4.setTitre("Festival Gnaoua");
            event4.setDescription("Festival de musique Gnaoua et musiques du monde");
            event4.setDateEvent(LocalDateTime.of(2026, 6, 22, 18, 0));
            event4.setLieu("Place Moulay Hassan, Essaouira");
            event4.setCapaciteTotal(10000);
            event4.setCapaciteDisponible(10000);
            event4.setPrix(100.0);
            event4.setCategory(culture);
            event4.setOrganisateurId(3L);
            event4.setImageUrl("https://example.com/gnaoua.jpg");
            eventRepository.save(event4);

            Event event5 = new Event();
            event5.setTitre("Marathon de Marrakech");
            event5.setDescription("Course internationale - 42km");
            event5.setDateEvent(LocalDateTime.of(2026, 5, 5, 7, 0));
            event5.setLieu("Marrakech");
            event5.setCapaciteTotal(5000);
            event5.setCapaciteDisponible(5000);
            event5.setPrix(200.0);
            event5.setCategory(sport);
            event5.setOrganisateurId(2L);
            event5.setImageUrl("https://example.com/marathon.jpg");
            eventRepository.save(event5);

            // Afficher les données
            System.out.println("\n📚 CATEGORIES:");
            List<Category> categories = categoryRepository.findAll();
            categories.forEach(cat -> System.out.println("  - " + cat.getNom() + " (" + cat.getIconUrl() + ")"));

            System.out.println("\n🎉 EVENTS:");
            List<Event> events = eventRepository.findAll();
            events.forEach(event -> System.out.println(
                    "  - [ID: " + event.getId() + "] " + event.getTitre() +
                            " | Date: " + event.getDateEvent() +
                            " | Places: " + event.getCapaciteDisponible() + "/" + event.getCapaciteTotal() +
                            " | Prix: " + event.getPrix() + " DH"
            ));

            System.out.println("\n========== Données chargées avec succès ! ==========\n");
        };
    }
}