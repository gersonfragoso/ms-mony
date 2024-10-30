package com.mony.order.mapper;

import com.mony.order.dto.OrderDTO;
import com.mony.order.model.OrderModel;

public class OrderMapper {

    public static OrderDTO toDTO(OrderModel order) {
        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                OrderItemMapper.toDTOList(order.getItems()),
                order.getCustomerId()
        );
    }

    public static OrderModel toEntity(OrderDTO orderDTO) {
        return new OrderModel(
                null,
                orderDTO.orderDate(),
                orderDTO.totalAmount(),
                orderDTO.status(),
                OrderItemMapper.toEntityList(orderDTO.items()),
                orderDTO.customerId()
        );
    }
}