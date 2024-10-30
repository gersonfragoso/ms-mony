package com.mony.order.dto;

import com.mony.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(Long id,
                       LocalDateTime orderDate,
                       BigDecimal totalAmount,
                       OrderStatus status,
                       List<OrderItemDTO> items,
                       Long customerId
                           )
{}
