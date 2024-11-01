package com.mony.payment.model;

import com.mony.payment.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    private String orderCode;

    private BigDecimal value;

    private String cpf;

    private String nameCard;

    private String number;

    private String dueDate;

    private String code;

    private LocalDateTime paymentDate;

    private UUID userId;

    private String nameUser;

    private String email;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
