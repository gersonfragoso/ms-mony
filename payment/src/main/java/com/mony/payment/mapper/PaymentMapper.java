package com.mony.payment.mapper;

import com.mony.payment.model.PaymentModel;
import com.mony.payment.model.dtos.PaymentReadDTO;
import com.mony.payment.model.dtos.PaymentWriteDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    // Converter de PaymentWriteDTO para PaymentModel
    // Usado para operações de escrita: de DTO para Entity, gravar no banco
    public static PaymentModel toEntity(PaymentWriteDTO paymentWriteDTO) {
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setOrderCode(paymentWriteDTO.orderCode()); // Assuming orderCode maps to orderCode
        paymentModel.setValue(paymentWriteDTO.value());
        paymentModel.setCpf(paymentWriteDTO.cpf());
        paymentModel.setNameCard(paymentWriteDTO.nameCard());
        paymentModel.setNumberCard(paymentWriteDTO.numberCard());
        paymentModel.setDueDate(paymentWriteDTO.dueDate());
        paymentModel.setCode(paymentWriteDTO.code());
        paymentModel.setUserId(paymentWriteDTO.userId());
        paymentModel.setNameUser(paymentWriteDTO.nameUser());
        paymentModel.setEmail(paymentWriteDTO.email());
        return paymentModel;
    }

    // Converter de PaymentModel para PaymentReadDTO
    // Para leitura, da Entity para DTO, realizar operações de recuperação dos dados.
    public static PaymentReadDTO toReadDTO(PaymentModel paymentModel) {
        PaymentReadDTO paymentReadDTO = new PaymentReadDTO();
        paymentReadDTO.setPaymentId(paymentModel.getPaymentId());
        paymentReadDTO.setOrderCode(paymentModel.getOrderCode());
        paymentReadDTO.setValue(paymentModel.getValue());
        paymentReadDTO.setCpf(paymentModel.getCpf());
        paymentReadDTO.setNumberCard(paymentModel.getNumberCard());
        paymentReadDTO.setPaymentDate(paymentModel.getPaymentDate());
        paymentReadDTO.setUserId(paymentModel.getUserId());
        paymentReadDTO.setNameUser(paymentModel.getNameUser());
        paymentReadDTO.setEmail(paymentModel.getEmail());
        paymentReadDTO.setPaymentStatus(paymentModel.getPaymentStatus().toString());
        return paymentReadDTO;
    }
}
