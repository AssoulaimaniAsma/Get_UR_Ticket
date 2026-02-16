package com.assoulaimani.eventservice.service;

import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.entity.EventStatus;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventMetricsService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final MeterRegistry meterRegistry;

    // ========== COMPTEURS (Counters) ==========
    private final Counter eventCreatedCounter;
    private final Counter eventApprovedCounter;
    private final Counter eventRejectedCounter;

    // ========== GAUGES (Valeurs en temps réel) ==========
    private final AtomicInteger totalEventsGauge = new AtomicInteger(0);
    private final AtomicInteger pendingEventsGauge = new AtomicInteger(0);
    private final AtomicInteger approvedEventsGauge = new AtomicInteger(0);
    private final AtomicInteger rejectedEventsGauge = new AtomicInteger(0);
    private final AtomicInteger fullEventsGauge = new AtomicInteger(0);

    public EventMetricsService(EventRepository eventRepository,
                               CategoryRepository categoryRepository,
                               MeterRegistry meterRegistry) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.meterRegistry = meterRegistry;

        // 📊 Compteurs - Incrémentés à chaque action
        this.eventCreatedCounter = Counter.builder("events.created.total")
                .description("Nombre total d'événements créés")
                .tag("service", "event-service")
                .register(meterRegistry);

        this.eventApprovedCounter = Counter.builder("events.approved.total")
                .description("Nombre d'événements approuvés")
                .tag("service", "event-service")
                .register(meterRegistry);

        this.eventRejectedCounter = Counter.builder("events.rejected.total")
                .description("Nombre d'événements rejetés")
                .tag("service", "event-service")
                .register(meterRegistry);

        // 📈 Gauges - Valeurs actuelles
        Gauge.builder("events.total.count", totalEventsGauge, AtomicInteger::get)
                .description("Nombre total d'événements dans la base")
                .tag("service", "event-service")
                .register(meterRegistry);

        Gauge.builder("events.pending.count", pendingEventsGauge, AtomicInteger::get)
                .description("Événements en attente de validation")
                .tag("service", "event-service")
                .tag("status", "PENDING")
                .register(meterRegistry);

        Gauge.builder("events.approved.count", approvedEventsGauge, AtomicInteger::get)
                .description("Événements approuvés et visibles")
                .tag("service", "event-service")
                .tag("status", "APPROVED")
                .register(meterRegistry);

        Gauge.builder("events.rejected.count", rejectedEventsGauge, AtomicInteger::get)
                .description("Événements rejetés")
                .tag("service", "event-service")
                .tag("status", "REJECTED")
                .register(meterRegistry);

        Gauge.builder("events.full.count", fullEventsGauge, AtomicInteger::get)
                .description("Événements complets (100% réservés)")
                .tag("service", "event-service")
                .register(meterRegistry);

        // 💰 Revenu potentiel total (calculé dynamiquement)
        Gauge.builder("events.potential.revenue.dh", eventRepository, this::calculatePotentialRevenue)
                .description("Revenu potentiel total en DH (Prix × Places disponibles)")
                .tag("service", "event-service")
                .tag("currency", "MAD")
                .register(meterRegistry);

        // 📊 Taux de remplissage moyen
        Gauge.builder("events.fill.rate.avg.percent", eventRepository, this::calculateAverageFillRate)
                .description("Taux de remplissage moyen des événements (%)")
                .tag("service", "event-service")
                .register(meterRegistry);

        // ✅ Taux d'approbation
        Gauge.builder("events.approval.rate.percent", eventRepository, this::calculateApprovalRate)
                .description("Taux d'approbation des événements (%)")
                .tag("service", "event-service")
                .register(meterRegistry);

        // 📅 Événements à venir (dans les 30 prochains jours)
        Gauge.builder("events.upcoming.30days.count", eventRepository, this::countUpcomingEvents)
                .description("Nombre d'événements dans les 30 prochains jours")
                .tag("service", "event-service")
                .tag("period", "30days")
                .register(meterRegistry);

        log.info("✅ EventMetricsService initialisé avec succès");

        // ✅ INITIALISER LES COMPTEURS AVEC LES DONNÉES EXISTANTES EN BASE H2
        initializeCountersFromDatabase();
    }

    /**
     * ✅ NOUVEAU : Initialiser les compteurs avec les événements déjà en base H2
     * Cette méthode lit votre base de données et initialise les compteurs Prometheus
     * avec les 20 événements existants
     */
    private void initializeCountersFromDatabase() {
        try {
            // Lire TOUS les événements de la base H2
            List<Event> allEvents = eventRepository.findAll();

            log.info("📊 Lecture de la base H2 : {} événements trouvés", allEvents.size());

            // Compter les événements par statut
            long approvedCount = allEvents.stream()
                    .filter(e -> e.getStatut() == EventStatus.APPROVED)
                    .count();

            long rejectedCount = allEvents.stream()
                    .filter(e -> e.getStatut() == EventStatus.REJECTED)
                    .count();

            long pendingCount = allEvents.stream()
                    .filter(e -> e.getStatut() == EventStatus.PENDING)
                    .count();

            // ✅ Initialiser les compteurs (simuler les incréments passés)
            // Compteur total d'événements créés
            for (int i = 0; i < allEvents.size(); i++) {
                eventCreatedCounter.increment();
            }

            // Compteur événements approuvés
            for (int i = 0; i < approvedCount; i++) {
                eventApprovedCounter.increment();
            }

            // Compteur événements rejetés
            for (int i = 0; i < rejectedCount; i++) {
                eventRejectedCounter.increment();
            }

            log.info("📊 ✅ Compteurs Prometheus initialisés depuis H2 :");
            log.info("   - Total créés : {}", allEvents.size());
            log.info("   - Approuvés   : {}", approvedCount);
            log.info("   - Rejetés     : {}", rejectedCount);
            log.info("   - En attente  : {}", pendingCount);

        } catch (Exception e) {
            log.error("❌ Erreur lors de l'initialisation des compteurs depuis H2: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== MISE À JOUR PÉRIODIQUE DES MÉTRIQUES ==========

    /**
     * Mise à jour toutes les 30 secondes
     */
    @Scheduled(fixedRate = 30000)
    public void updateMetrics() {
        try {
            totalEventsGauge.set((int) eventRepository.count());
            pendingEventsGauge.set(eventRepository.findByStatut(EventStatus.PENDING).size());
            approvedEventsGauge.set(eventRepository.findByStatut(EventStatus.APPROVED).size());
            rejectedEventsGauge.set(eventRepository.findByStatut(EventStatus.REJECTED).size());

            // Compter les événements complets
            long fullEvents = eventRepository.findAll().stream()
                    .filter(e -> e.getCapaciteDisponible() == 0 && e.getStatut() == EventStatus.APPROVED)
                    .count();
            fullEventsGauge.set((int) fullEvents);

            log.debug("📊 Métriques mises à jour - Total: {}, Pending: {}, Approved: {}, Rejected: {}, Full: {}",
                    totalEventsGauge.get(), pendingEventsGauge.get(), approvedEventsGauge.get(),
                    rejectedEventsGauge.get(), fullEventsGauge.get());

        } catch (Exception e) {
            log.error("❌ Erreur lors de la mise à jour des métriques: {}", e.getMessage());
        }
    }

    /**
     * Mise à jour des métriques par catégorie (toutes les minutes)
     */
    @Scheduled(fixedRate = 60000)
    public void updateCategoryMetrics() {
        try {
            List<Event> approvedEvents = eventRepository.findByStatut(EventStatus.APPROVED);

            // Grouper par catégorie
            Map<String, Long> eventsByCategory = approvedEvents.stream()
                    .filter(e -> e.getCategory() != null)
                    .collect(Collectors.groupingBy(
                            e -> e.getCategory().getNom(),
                            Collectors.counting()
                    ));

            // Enregistrer une gauge pour chaque catégorie
            eventsByCategory.forEach((category, count) -> {
                meterRegistry.gauge("events.by.category.count",
                        List.of(
                                io.micrometer.core.instrument.Tag.of("category", category),
                                io.micrometer.core.instrument.Tag.of("service", "event-service")
                        ),
                        count);
            });

            log.debug("📊 Métriques par catégorie mises à jour: {}", eventsByCategory);

        } catch (Exception e) {
            log.error("❌ Erreur lors de la mise à jour des métriques par catégorie: {}", e.getMessage());
        }
    }

    /**
     * Mise à jour des métriques par organisateur (toutes les 2 minutes)
     */
    @Scheduled(fixedRate = 120000)
    public void updateOrganizerMetrics() {
        try {
            List<Event> events = eventRepository.findAll();

            // Top 5 organisateurs par nombre d'événements
            Map<Long, Long> eventsByOrganizer = events.stream()
                    .filter(e -> e.getOrganisateurId() != null)
                    .collect(Collectors.groupingBy(
                            Event::getOrganisateurId,
                            Collectors.counting()
                    ));

            eventsByOrganizer.forEach((organizerId, count) -> {
                meterRegistry.gauge("events.by.organizer.count",
                        List.of(
                                io.micrometer.core.instrument.Tag.of("organizer_id", String.valueOf(organizerId)),
                                io.micrometer.core.instrument.Tag.of("service", "event-service")
                        ),
                        count);
            });

            log.debug("📊 Métriques par organisateur mises à jour: {} organisateurs", eventsByOrganizer.size());

        } catch (Exception e) {
            log.error("❌ Erreur lors de la mise à jour des métriques par organisateur: {}", e.getMessage());
        }
    }

    // ========== MÉTHODES PUBLIQUES POUR INCRÉMENTER LES COMPTEURS ==========

    /**
     * Appelé lors de la création d'un événement
     */
    public void incrementEventCreated() {
        eventCreatedCounter.increment();
        log.debug("📊 Compteur events.created.total incrémenté");
    }

    /**
     * Appelé lors de l'approbation d'un événement
     */
    public void incrementEventApproved() {
        eventApprovedCounter.increment();
        log.debug("📊 Compteur events.approved.total incrémenté");
    }

    /**
     * Appelé lors du rejet d'un événement
     */
    public void incrementEventRejected() {
        eventRejectedCounter.increment();
        log.debug("📊 Compteur events.rejected.total incrémenté");
    }

    // ========== TIMER POUR MESURER LA DURÉE DE VALIDATION ==========

    /**
     * Démarrer un timer pour mesurer la durée de validation
     */
    public Timer.Sample startValidationTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Enregistrer la durée de validation
     */
    public void recordValidationDuration(Timer.Sample sample, String status) {
        sample.stop(Timer.builder("events.validation.duration.seconds")
                .description("Durée de validation d'un événement en secondes")
                .tag("status", status)
                .tag("service", "event-service")
                .register(meterRegistry));
        log.debug("⏱️ Durée de validation enregistrée pour statut: {}", status);
    }

    // ========== CALCULS DE MÉTRIQUES COMPLEXES ==========

    /**
     * Calcul du revenu potentiel total
     */
    private double calculatePotentialRevenue(EventRepository repo) {
        try {
            return repo.findByStatut(EventStatus.APPROVED).stream()
                    .mapToDouble(e -> e.getPrix() * e.getCapaciteDisponible())
                    .sum();
        } catch (Exception e) {
            log.error("❌ Erreur calcul revenu potentiel: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Calcul du taux de remplissage moyen
     */
    private double calculateAverageFillRate(EventRepository repo) {
        try {
            List<Event> events = repo.findByStatut(EventStatus.APPROVED);
            if (events.isEmpty()) return 0.0;

            double avgFillRate = events.stream()
                    .filter(e -> e.getCapaciteTotal() > 0)
                    .mapToDouble(e -> {
                        int reserved = e.getCapaciteTotal() - e.getCapaciteDisponible();
                        return (reserved * 100.0) / e.getCapaciteTotal();
                    })
                    .average()
                    .orElse(0.0);

            return Math.round(avgFillRate * 100.0) / 100.0;

        } catch (Exception e) {
            log.error("❌ Erreur calcul taux de remplissage: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Calcul du taux d'approbation
     */
    private double calculateApprovalRate(EventRepository repo) {
        try {
            long total = repo.count();
            if (total == 0) return 0.0;

            long approved = repo.findByStatut(EventStatus.APPROVED).size();
            return Math.round((approved * 100.0 / total) * 100.0) / 100.0;

        } catch (Exception e) {
            log.error("❌ Erreur calcul taux d'approbation: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Compter les événements à venir dans les 30 prochains jours
     */
    private double countUpcomingEvents(EventRepository repo) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime in30Days = now.plusDays(30);

            return repo.findByStatut(EventStatus.APPROVED).stream()
                    .filter(e -> e.getDateEvent() != null)
                    .filter(e -> e.getDateEvent().isAfter(now) && e.getDateEvent().isBefore(in30Days))
                    .count();

        } catch (Exception e) {
            log.error("❌ Erreur calcul événements à venir: {}", e.getMessage());
            return 0.0;
        }
    }

    // ========== MÉTRIQUES DE DÉLAI ==========

    /**
     * Calcul du délai moyen de validation (PENDING → APPROVED/REJECTED)
     * Note: Nécessite d'ajouter des champs createdAt/validatedAt dans Event
     */
    public void recordValidationDelay(Event event) {
        if (event.getCreatedAt() != null && event.getUpdatedAt() != null) {
            Duration delay = Duration.between(event.getCreatedAt(), event.getUpdatedAt());

            meterRegistry.timer("events.validation.delay.seconds",
                    "status", event.getStatut().toString(),
                    "service", "event-service"
            ).record(delay);

            log.debug("⏱️ Délai de validation enregistré: {} secondes pour event ID: {}",
                    delay.getSeconds(), event.getId());
        }
    }
}