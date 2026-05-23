package com.technokratos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

    @RestController
    static class TestController {
        private final TestService service;

        public TestController(TestService service) {
            this.service = service;
        }

        @GetMapping("/test")
        public String test() {
            return service.findAll("Testing data");
        }
    }

    @Service
    static class TestService {
        private final TestRepository repository;

        public TestService(TestRepository repository) {
            this.repository = repository;
        }

        public String findAll(String input) {
            repository.findAll();
            return input;
        }
    }

    @Repository
    static class TestRepository {
        public List<String> findAll() {
            return Arrays.asList("apple, banana, orange, water");
        }
    }
}

