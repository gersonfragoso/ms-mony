package com.mony.notification.consumer;

import com.mony.notification.dtos.PaymentEmailConfirmationDto;
import com.mony.notification.model.EmailModel;
import com.mony.notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {
    private final EmailService emailService;

    public PaymentConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name.confirmation}")
    public void listenPaymentConfirmation (@Payload PaymentEmailConfirmationDto paymentConfDto) {
        EmailModel emailModel = emailService.createPaymentConfirmationEmail(paymentConfDto);
        emailService.sendEmail(emailModel);
    }
}
