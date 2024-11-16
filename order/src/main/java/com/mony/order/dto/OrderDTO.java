package com.mony.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    @PastOrPresent(message = "A data do pedido não pode ser no futuro")
    private LocalDate orderDate;

    private BigDecimal totalAmount;

    @Valid
    private List<OrderItemDTO> items;

    private UUID customerId;

    private UUID id;

    private String status;

    // Construtor padrão (sem argumentos) para permitir desserialização
    public OrderDTO() {
        this.items = new ArrayList<>(); // Inicializa a lista de itens para evitar NPE
    }

    // Construtor
    public OrderDTO(UUID id, LocalDate orderDate, BigDecimal totalAmount, UUID customerId, List<OrderItemDTO> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        // Inicializando a lista de itens para evitar NPE
        if (items == null) {
            this.items = new ArrayList<>(); // Evita o NullPointerException
        } else {
            this.items = items;
        }
    }

    public OrderDTO(LocalDate orderDate, BigDecimal totalAmount, UUID customerId, List<OrderItemDTO> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        // Inicializando a lista de itens para evitar NPE
        if (items == null) {
            this.items = new ArrayList<>(); // Evita o NullPointerException
        } else {
            this.items = items;
        }
    }

    // Método toString, se necessário para exibir os valores do objeto em forma de string
    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                ", customerId=" + customerId +
                '}';
    }

    // Método equals e hashCode, se necessário para comparações e operações em coleções (ex. HashMap)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id.equals(orderDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
