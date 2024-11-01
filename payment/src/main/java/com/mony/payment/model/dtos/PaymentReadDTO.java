package com.mony.payment.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReadDTO {
    private UUID paymentId;
    private String orderCode;
    private BigDecimal value;
    private String cpf;
    private String numberCard;
    private LocalDateTime paymentDate;
    private UUID userId;
    private String nameUser;
    private String email;
}
