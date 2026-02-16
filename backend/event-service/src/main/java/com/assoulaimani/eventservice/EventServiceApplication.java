package com.assoulaimani.eventservice;

import com.assoulaimani.eventservice.entity.Category;
import com.assoulaimani.eventservice.entity.Event;
import com.assoulaimani.eventservice.entity.EventStatus;
import com.assoulaimani.eventservice.repository.CategoryRepository;
import com.assoulaimani.eventservice.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling  // ✅ Activer les tâches planifiées (@Scheduled)

public class EventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }


}