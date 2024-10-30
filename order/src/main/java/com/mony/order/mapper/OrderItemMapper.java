package com.mony.order.mapper;

import com.mony.order.dto.OrderItemDTO;
import com.mony.order.model.OrderItemModel;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

    public static OrderItemDTO toDTO(OrderItemModel orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getOrder().getId()
        );
    }

    public static OrderItemModel toEntity(OrderItemDTO orderItemDTO) {
        return new OrderItemModel(
                null,
                orderItemDTO.productName(),
                orderItemDTO.quantity(),
                orderItemDTO.price(),
                null
        );
    }

    public static List<OrderItemDTO> toDTOList(List<OrderItemModel> orderItems) {
        return orderItems.stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<OrderItemModel> toEntityList(List<OrderItemDTO> orderItemDTOs) {
        return orderItemDTOs.stream()
                .map(OrderItemMapper::toEntity)
                .collect(Collectors.toList());
    }
}