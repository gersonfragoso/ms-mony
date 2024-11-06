package com.mony.payment.producer;

import com.mony.payment.model.PaymentModel;
import com.mony.payment.model.dtos.EmailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name.confirmation}")
    private String routingKey;

    public void publishMessageEmail(PaymentModel paymentModel) {
        EmailDTO emailDTO = new EmailDTO(paymentModel.getPaymentId(), paymentModel.getOrderCode(),
                paymentModel.getUserId(), paymentModel.getNameUser(),
                paymentModel.getEmail(), paymentModel.getValue(), paymentModel.getPaymentStatus().toString());

        rabbitTemplate.convertAndSend("", routingKey, emailDTO);
    }


}
