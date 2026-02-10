package com.assoulaimani.reservationservice.client;

import com.assoulaimani.reservationservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/email/{email}")
    User getUserByEmail(@PathVariable("email") String email);
}