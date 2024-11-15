package com.mony.order.service;

import com.mony.order.dto.OrderDTO;
import com.mony.order.enums.OrderStatus;
import com.mony.order.model.OrderModel;
import com.mony.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnSavedOrder() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO(
                null,
                LocalDate.now(),
                BigDecimal.valueOf(100.0),
                OrderStatus.PENDING,
                Collections.emptyList(),
                1L
        );

        OrderModel savedOrder = new OrderModel(
                1L,
                orderDTO.orderDate(),
                orderDTO.totalAmount(),
                orderDTO.status(),
                null,
                orderDTO.customerId()
        );

        when(orderRepository.save(any(OrderModel.class))).thenReturn(savedOrder);

        // Act
        OrderDTO result = orderService.createOrder(orderDTO);

        // Assert
        assertEquals(savedOrder.getId(), result.id());
        assertEquals(savedOrder.getTotalAmount(), result.totalAmount());
    }
}
