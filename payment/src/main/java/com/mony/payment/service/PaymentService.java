package com.mony.payment.service;

import com.mony.payment.mapper.PaymentMapper;
import com.mony.payment.model.PaymentModel;
import com.mony.payment.model.dtos.PaymentReadDTO;
import com.mony.payment.model.dtos.PaymentWriteDTO;
import com.mony.payment.model.enums.PaymentStatus;
import com.mony.payment.producer.PaymentProducer;
import com.mony.payment.repository.PaymentRepository;
import com.mony.payment.service.paymentgateway.PaymentGateway;
import exception.PaymentProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
    private final PaymentGateway paymentGateway;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentProducer paymentProducer, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
        this.paymentGateway = paymentGateway;
    }

    public PaymentReadDTO processPayment(PaymentWriteDTO paymentWriteDTO) {
        PaymentModel paymentModel = PaymentMapper.toEntity(paymentWriteDTO);
        paymentModel.setPaymentDate(LocalDateTime.now());
        paymentModel.setPaymentStatus(PaymentStatus.CONFIRMED);

        try {
            if (!paymentGateway.processPayment(paymentWriteDTO)) {
                paymentModel.setPaymentStatus(PaymentStatus.CANCELLED);
            }

            paymentModel = paymentRepository.save(paymentModel);

            paymentProducer.publishMessageEmail(paymentModel);

        } catch (Exception e) {
            paymentModel.setPaymentStatus(PaymentStatus.CANCELLED);
            throw new PaymentProcessingException("Erro no processamento do pagamento. Operação Cancelada.", e);
        }

        return PaymentMapper.toReadDTO(paymentModel);
    }


}
