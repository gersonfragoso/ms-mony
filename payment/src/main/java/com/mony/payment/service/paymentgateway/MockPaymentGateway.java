package com.mony.payment.service.paymentgateway;

import com.mony.payment.model.dtos.PaymentWriteDTO;
import org.springframework.stereotype.Service;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public boolean processPayment(PaymentWriteDTO paymentWriteDTO) {
        //Simula um pagamento bem sucedido.
        //Pode futuramente implementar uma lógica real de pagamento, utilizando alguma API externa.
        return true;
    }
}
