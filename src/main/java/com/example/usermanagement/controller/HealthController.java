package com.example.usermanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final Instant startTime = Instant.now();
    private static final String VERSION = "Auth-Service-1.0.0"; // ⚙️ Cambia según el servicio

    @GetMapping("/health")
    public Map<String, Object> health() {
        return buildHealthResponse("UP", "Service is running normally");
    }

    @GetMapping("/health/live")
    public Map<String, Object> live() {
        return buildHealthResponse("UP", "Service is alive");
    }

    @GetMapping("/health/ready")
    public Map<String, Object> ready() {
        return buildHealthResponse("UP", "Service is ready to handle requests");
    }

    private Map<String, Object> buildHealthResponse(String status, String description) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("description", description);
        response.put("version", VERSION);
        response.put("uptime", getUptime());
        response.put("timestamp", Instant.now().toString());
        return response;
    }

    private String getUptime() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration uptime = Duration.ofMillis(uptimeMs);
        return String.format("%d min, %d sec", uptime.toMinutes(), uptime.toSecondsPart());
    }
}