package com.huzaifah.weather_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        log.info("Health check requested");
        Map<String, String> response = Map.of("status", "UP");
        log.info("Health check returning: {}", response);
        return response;
    }
}
