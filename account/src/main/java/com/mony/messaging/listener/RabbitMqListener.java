package com.mony.messaging.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMqListener {

    @RabbitListener(queues = "otpQueue")
    public void receiveMessage(Map<String, String> message) {
        String email = message.get("email");
        String otp = message.get("otp");

        // Aqui você pode enviar o OTP por e-mail ou SMS
        System.out.println("Enviando OTP para " + email + ": " + otp);
    }
}
