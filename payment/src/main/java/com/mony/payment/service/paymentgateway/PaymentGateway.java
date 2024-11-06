package com.mony.payment.service.paymentgateway;

import com.mony.payment.model.dtos.PaymentWriteDTO;

public interface PaymentGateway {
    //Interface para simular o processamento do pagamento.
    boolean processPayment(PaymentWriteDTO paymentWriteDTO);
}
