package com.example.usermanagement.service;

import com.example.usermanagement.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String eventType, Object payload) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", eventType);
        event.put("timestamp", Instant.now().toString());
        event.put("data", payload);

        rabbitTemplate.convertAndSend(RabbitConfig.USER_EVENTS_QUEUE, event);
        System.out.println("📤 Evento publicado: " + eventType);
    }
}
