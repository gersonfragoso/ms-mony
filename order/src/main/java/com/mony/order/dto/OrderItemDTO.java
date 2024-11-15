package com.mony.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OrderItemDTO (Long id,
                            @NotBlank(message = "O nome do produto é obrigatório") String productName,
                            @Min(value = 1, message = "A quantidade deve ser pelo menos 1") Integer quantity,
                            @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero") BigDecimal price,
                            Long orderId
) {}
