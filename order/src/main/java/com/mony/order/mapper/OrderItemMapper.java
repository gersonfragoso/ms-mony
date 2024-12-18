package com.mony.order.mapper;

import com.mony.order.dto.OrderItemDTO;
import com.mony.order.model.OrderItemModel;
import com.mony.order.model.OrderModel;
import org.springframework.stereotype.Component;


@Component
public class OrderItemMapper {

    public static OrderItemModel toModel(OrderItemDTO orderItemDTO) {
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setId(orderItemDTO.id());
        orderItemModel.setProductName(orderItemDTO.productName());
        orderItemModel.setQuantity(orderItemDTO.quantity());
        orderItemModel.setPrice(orderItemDTO.price());

        OrderModel order = new OrderModel();
        order.setId(orderItemDTO.orderId());
        orderItemModel.setOrder(order);

        return orderItemModel;
    }

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