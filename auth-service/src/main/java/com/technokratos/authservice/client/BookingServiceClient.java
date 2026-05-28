package com.technokratos.authservice.client;

import com.technokratos.authservice.entity.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "booking-service", url = "http://booking-service:8082")
public interface BookingServiceClient {

    @PostMapping("/api/v1/internal/users")
    void createProfile(@RequestParam("email") String email, @RequestParam("name") String name,
                       @RequestParam("role") Role role);
}