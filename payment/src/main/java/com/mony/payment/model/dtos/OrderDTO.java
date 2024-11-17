package com.mony.payment.model.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class OrderDTO {
    //private Long orderId;
    private UUID orderId;
    private BigDecimal totalAmount;
    private UUID customerId;
    private String status;
    private LocalDate orderDate;

    OrderDTO(UUID orderId, BigDecimal totalAmount, UUID customerId, String status, LocalDate orderDate) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.status = status;
        this.orderDate = orderDate;
    }




}
