package com.mony.payment.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    //private Long orderId;
    private UUID orderId;
    private BigDecimal totalAmount;

    private UUID customerId;




}
