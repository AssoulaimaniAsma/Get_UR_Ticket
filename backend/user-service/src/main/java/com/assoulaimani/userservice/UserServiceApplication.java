package com.assoulaimani.userservice;

import com.assoulaimani.userservice.entity.Role;
import com.assoulaimani.userservice.entity.User;
import com.assoulaimani.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner loadUserData(UserRepository userRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("========== Chargement des données de test (USER SERVICE) ==========");

            // Admin
            User admin = new User();
            admin.setNom("Admin System");
            admin.setEmail("admin@eventbooking.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setTelephone("0612345678");
            admin.setAdresse("123 Rue Admin, Rabat");
            userRepository.save(admin);

            // Organisateur 1
            User organizer1 = new User();
            organizer1.setNom("Mohammed Alami");
            organizer1.setEmail("m.alami@events.ma");
            organizer1.setPassword(passwordEncoder.encode("organizer123"));
            organizer1.setRole(Role.ORGANIZER);
            organizer1.setTelephone("0623456789");
            organizer1.setAdresse("45 Avenue Hassan II, Casablanca");
            userRepository.save(organizer1);

            // Organisateur 2
            User organizer2 = new User();
            organizer2.setNom("Fatima Zahra");
            organizer2.setEmail("f.zahra@sports.ma");
            organizer2.setPassword(passwordEncoder.encode("organizer123"));
            organizer2.setRole(Role.ORGANIZER);
            organizer2.setTelephone("0634567890");
            organizer2.setAdresse("78 Boulevard Zerktouni, Casablanca");
            userRepository.save(organizer2);

            // Organisateur 3
            User organizer3 = new User();
            organizer3.setNom("Ahmed Bennani");
            organizer3.setEmail("a.bennani@culture.ma");
            organizer3.setPassword(passwordEncoder.encode("organizer123"));
            organizer3.setRole(Role.ORGANIZER);
            organizer3.setTelephone("0645678901");
            organizer3.setAdresse("12 Rue de la Liberté, Essaouira");
            userRepository.save(organizer3);

            // Utilisateur 1
            User user1 = new User();
            user1.setNom("Youssef Idrissi");
            user1.setEmail("youssef.idrissi@gmail.com");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setRole(Role.USER);
            user1.setTelephone("0656789012");
            user1.setAdresse("34 Quartier Agdal, Rabat");
            userRepository.save(user1);

            // Utilisateur 2
            User user2 = new User();
            user2.setNom("Salma El Fassi");
            user2.setEmail("salma.elfassi@gmail.com");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.setRole(Role.USER);
            user2.setTelephone("0667890123");
            user2.setAdresse("56 Maarif, Casablanca");
            userRepository.save(user2);

            // Utilisateur 3
            User user3 = new User();
            user3.setNom("Karim Benjelloun");
            user3.setEmail("karim.b@hotmail.com");
            user3.setPassword(passwordEncoder.encode("user123"));
            user3.setRole(Role.USER);
            user3.setTelephone("0678901234");
            user3.setAdresse("89 Gueliz, Marrakech");
            userRepository.save(user3);

            // Afficher les données
            System.out.println("\n👥 UTILISATEURS:");
            List<User> users = userRepository.findAll();
            users.forEach(user -> System.out.println(
                    "  - [ID: " + user.getId() + "] " + user.getNom() +
                            " | Email: " + user.getEmail() +
                            " | Role: " + user.getRole() +
                            " | Tel: " + user.getTelephone()
            ));

            System.out.println("\n🔑 Credentials de test:");
            System.out.println("  ADMIN:      admin@eventbooking.com / admin123");
            System.out.println("  ORGANIZER:  m.alami@events.ma / organizer123");
            System.out.println("  USER:       youssef.idrissi@gmail.com / user123");

            System.out.println("\n========== Données chargées avec succès ! ==========\n");
        };
    }
}