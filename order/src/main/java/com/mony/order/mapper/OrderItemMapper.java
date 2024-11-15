package com.mony.order.mapper;

import com.mony.order.dto.OrderItemDTO;
import com.mony.order.model.OrderItemModel;
import org.springframework.stereotype.Component;


@Component
public class OrderItemMapper {

    // Converte de OrderItemDTO para OrderItemModel
    public static OrderItemModel toModel(OrderItemDTO orderItemDTO) {
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setId(orderItemDTO.id());
        orderItemModel.setProductName(orderItemDTO.productName());
        orderItemModel.setQuantity(orderItemDTO.quantity());
        orderItemModel.setPrice(orderItemDTO.price());

        return orderItemModel;
    }

    // Converte de OrderItemModel para OrderItemDTO
    public static OrderItemDTO toDTO(OrderItemModel orderItemModel) {
        return new OrderItemDTO(
                orderItemModel.getId(),
                orderItemModel.getProductName(),
                orderItemModel.getQuantity(),
                orderItemModel.getPrice(),
                orderItemModel.getOrder().getId()
        );
    }
}