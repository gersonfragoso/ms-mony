package com.mony.order.dto;

import com.mony.order.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record OrderDTO(
        Long id,
        @NotNull(message = "A data do pedido não pode ser nula")
        @PastOrPresent(message = "A data do pedido não pode ser no futuro")
        LocalDate orderDate,

        @DecimalMin(value = "0.0", inclusive = false, message = "O total do pedido deve ser maior que zero")
        BigDecimal totalAmount,

        @NotNull(message = "O status do pedido é obrigatório")
        OrderStatus status,

        @NotNull(message = "A lista de itens não pode ser nula")
        @Valid
        List<OrderItemDTO> items,

        @NotNull(message = "O ID do cliente é obrigatório")
        UUID customerId
) {
    // Inicializando a lista de itens para evitar NPE (NullPointerException)
    public OrderDTO {
        if (items == null) {
            items = new ArrayList<>();  // Evita o NullPointerException
        }
    }
}