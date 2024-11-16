package com.mony.order.mapper;

import com.mony.order.dto.OrderDTO;
import com.mony.order.dto.OrderItemDTO;
import com.mony.order.model.OrderItemModel;
import com.mony.order.model.OrderModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // Converte de OrderDTO para OrderModel
    public static OrderModel toModel(OrderDTO orderDTO) {
        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderDTO.id());
        orderModel.setOrderDate(orderDTO.orderDate());
        orderModel.setTotalAmount(orderDTO.totalAmount());
        orderModel.setCustomerId(UUID.fromString(orderDTO.customerId().toString()));

        // Converte os itens manualmente
        List<OrderItemModel> items = orderDTO.items().stream()
                .map(OrderItemMapper::toModel)
                .collect(Collectors.toList());
        orderModel.setItems(items);

        return orderModel;
    }

    // Converte de OrderModel para OrderDTO
    public static OrderDTO toDTO(OrderModel orderModel) {
        List<OrderItemDTO> itemsDTO = orderModel.getItems().stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                orderModel.getId(),
                orderModel.getOrderDate(),
                orderModel.getTotalAmount(),
                itemsDTO,
                orderModel.getCustomerId()
        );
    }

    // Método para converter uma lista de OrderModels para OrderDTOs
    public static List<OrderDTO> toDTOList(List<OrderModel> orderModels) {
        return orderModels.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Método para converter uma lista de OrderDTOs para OrderModels
    public static List<OrderModel> toModelList(List<OrderDTO> orderDTOs) {
        return orderDTOs.stream()
                .map(OrderMapper::toModel)
                .collect(Collectors.toList());
    }
}