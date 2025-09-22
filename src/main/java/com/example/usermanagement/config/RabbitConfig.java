package com.example.usermanagement.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    public static final String USER_EVENTS_EXCHANGE = "user.events.exchange";
    //public static final String USER_EVENTS_QUEUE = "user.events.queue";
    public static final String USER_EVENTS_ROUTING_KEY = "user.events.routingkey";

    @Bean
    public DirectExchange userEventsExchange() {
        return new DirectExchange(USER_EVENTS_EXCHANGE);
    }

    /*@Bean
    public Queue userEventsQueue() {
        return new Queue(USER_EVENTS_QUEUE);
    }

    @Bean
    public Binding bindingUserEvents(Queue userEventsQueue, DirectExchange userEventsExchange) {
        return BindingBuilder.bind(userEventsQueue).to(userEventsExchange).with(USER_EVENTS_ROUTING_KEY);
    }*/
}
