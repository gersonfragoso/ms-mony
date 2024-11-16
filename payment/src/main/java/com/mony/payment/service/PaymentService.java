package com.mony.payment.service;

import com.mony.payment.integration.OrderFeignClient;
import com.mony.payment.mapper.PaymentMapper;
import com.mony.payment.model.PaymentModel;
import com.mony.payment.model.dtos.OrderDTO;
import com.mony.payment.model.dtos.PaymentReadDTO;
import com.mony.payment.model.dtos.PaymentWriteDTO;
import com.mony.payment.model.enums.PaymentStatus;
import com.mony.payment.producer.PaymentProducer;
import com.mony.payment.repository.PaymentRepository;
import com.mony.payment.service.paymentgateway.PaymentGateway;
import com.mony.payment.exception.PaymentProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;
    private final PaymentGateway paymentGateway;
    private final OrderFeignClient orderFeignClient;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentProducer paymentProducer,
                          PaymentGateway paymentGateway, OrderFeignClient orderFeignClient) {
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
        this.paymentGateway = paymentGateway;
        this.orderFeignClient = orderFeignClient;
    }

    public OrderDTO getOrderByIdFromFeign(UUID orderId) {
        try{
            OrderDTO orderDTO;
            orderDTO = orderFeignClient.getOrderById(orderId);
            return orderDTO;
        } catch(EntityNotFoundException e){
            return null;
        }
    }

    public boolean compareUserId(UUID userIdFromOrder, UUID userIdFromToken){
        return userIdFromOrder.equals(userIdFromToken);
    }



    public PaymentReadDTO processPayment(PaymentWriteDTO paymentWriteDTO) {
        PaymentModel paymentModel = PaymentMapper.toEntity(paymentWriteDTO);
        paymentModel.setPaymentDate(LocalDateTime.now());
        paymentModel.setPaymentStatus(PaymentStatus.CONFIRMED);

        try {
            if(!verifyDueDateCard(paymentWriteDTO.dueDate())) {
                paymentModel.setPaymentStatus(PaymentStatus.CANCELLED);
                System.out.println("Cartao vencido");
            }
            else if (!paymentGateway.processPayment(paymentWriteDTO)){
                paymentModel.setPaymentStatus(PaymentStatus.CANCELLED);
            }

            paymentModel = paymentRepository.save(paymentModel);
            paymentProducer.publishMessageEmail(paymentModel);

        } catch (Exception e) {
            paymentModel.setPaymentStatus(PaymentStatus.CANCELLED);
            paymentModel = paymentRepository.save(paymentModel);
            throw new PaymentProcessingException("Erro no processamento do pagamento. Operação Cancelada.", e);
        }

        return formatPaymentReadDTO(PaymentMapper.toReadDTO(paymentModel));
    }

    public PaymentReadDTO getPaymentById(UUID paymentId) {
        Optional<PaymentModel> optionalPaymentModel = paymentRepository.findById(paymentId);

        if (optionalPaymentModel.isPresent()) {
            return formatPaymentReadDTO(PaymentMapper.toReadDTO(optionalPaymentModel.get()));
        } else {
            throw new EntityNotFoundException("Pagamento não encontrado com ID: " + paymentId);
        }
    }

    public Page<PaymentReadDTO> getPaymentsByCpf(String cpf, Pageable pageable) {
        Optional<Page<PaymentModel>> optionalPaymentPage = paymentRepository.findAllByCpf(cpf, pageable);

        //se encontrou resultados...
        if (optionalPaymentPage.isPresent()) {
            return optionalPaymentPage.get().map(paymentModel -> formatPaymentReadDTO(PaymentMapper.toReadDTO(paymentModel)));
        } else {
            // retorna uma página vazia caso não encontre pagamentos do cpf informado
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    private String formatNumberCard(String numberCard) {
        return numberCard.replace(numberCard.substring(4, 12), "********");
    }

    private PaymentReadDTO formatPaymentReadDTO(PaymentReadDTO paymentReadDTO) {
        paymentReadDTO.setNumberCard(formatNumberCard(paymentReadDTO.getNumberCard()));
        return paymentReadDTO;
    }

    //Verifica se o cartõo é válido
    private boolean verifyDueDateCard(String dueDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth today = YearMonth.now();
        YearMonth dueDateFormatted = YearMonth.parse(dueDate, formatter);
        return dueDateFormatted.isAfter(today);
    }
}
