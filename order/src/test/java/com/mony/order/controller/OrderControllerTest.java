package com.mony.order.controller;
/*
import com.mony.order.dto.OrderDTO;
import com.mony.order.enums.OrderStatus;
import com.mony.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrder_ShouldReturn201_WhenRequestIsValid() throws Exception {
        // Arrange
        OrderDTO orderDTO = new OrderDTO(
                null,
                LocalDate.now(),
                BigDecimal.valueOf(100.0),
                OrderStatus.PENDING,
                Collections.emptyList(),
                1L
        );

        Mockito.when(orderService.createOrder(Mockito.any(OrderDTO.class))).thenReturn(orderDTO);

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "orderDate": "2024-11-12",
                                  "totalAmount": 100.0,
                                  "status": "PENDING",
                                  "customerId": 1,
                                  "items": []
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
*/