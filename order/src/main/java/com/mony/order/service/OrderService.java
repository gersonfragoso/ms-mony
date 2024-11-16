package com.mony.order.service;

import com.mony.order.dto.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO, UUID userId);
    OrderDTO updateOrder(UUID orderId, OrderDTO orderDTO);
    OrderDTO getOrderById(UUID orderId);
    List<OrderDTO> getAllOrders();
    void deleteOrder(UUID orderId);
}
