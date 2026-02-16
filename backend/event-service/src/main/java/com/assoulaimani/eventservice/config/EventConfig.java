package com.assoulaimani.eventservice.config;

import com.assoulaimani.eventservice.entity.Category;
import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.entity.EventStatus;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class EventConfig {

    @Bean
    public CommandLineRunner loadEventData(CategoryRepository categoryRepository,
                                           EventRepository eventRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
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
                event1.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSCErQJj0ZvbMOCrOaSyGNI4GnKdINV8qnfuA&s");
                event1.setStatut(EventStatus.APPROVED); // ✅ Événement approuvé
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
                event2.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGGICyVoieoY5tFkKn6rOFyQlSXhMZX-Cb9w&s");
                event2.setStatut(EventStatus.APPROVED); // ✅ Événement approuvé
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
                event3.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTK_U94Vg-uJuWGag2P4Nr0EE_Cb_TIjMtBNg&s");
                event3.setStatut(EventStatus.PENDING); // ✅ En attente d'approbation
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
                event4.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWEdoonoV_e__ZQGwUrDskSxQMuprgvbozGg&s");
                event4.setStatut(EventStatus.APPROVED); // ✅ Événement approuvé
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
                event5.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSv3ck8qJXlXmKAv11lTiBa5_9L90Qc5Irt5Q&s");
                event5.setStatut(EventStatus.REJECTED); // ✅ Événement rejeté (exemple)
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
                                " | Prix: " + event.getPrix() + " DH" +
                                " | Statut: " + event.getStatut() // ✅ Afficher le statut
                ));

            System.out.println("\n========== Données chargées avec succès ! ==========\n");}
            else {
                System.out.println("========== Données déjà présentes, chargement ignoré ==========");
            }
        };
    }
}