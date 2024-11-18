package com.mony.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue otpQueue() {
        return new Queue("otpQueue", true);
    }

    @Bean
    public DirectExchange otpExchange() {
        return new DirectExchange("otpExchange");
    }

    @Bean
    public Binding binding(Queue otpQueue, DirectExchange otpExchange) {
        return BindingBuilder.bind(otpQueue).to(otpExchange).with("otpRoutingKey");
    }
}

