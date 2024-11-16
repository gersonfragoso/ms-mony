package com.mony.payment.model.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
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

    OrderDTO(UUID orderId, BigDecimal totalAmount, UUID customerId, String status) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.status = status;
    }




}
