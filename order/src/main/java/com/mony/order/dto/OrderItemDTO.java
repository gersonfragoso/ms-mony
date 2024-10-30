package com.mony.order.dto;

import java.math.BigDecimal;

public record OrderItemDTO(Long id,
                           String productName,
                           int quantity,
                           BigDecimal price,
                           Long orderId
                           ) {
}
