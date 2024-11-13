package com.mony.order.service.service_impl;

import com.mony.order.dto.OrderDTO;
import com.mony.order.dto.OrderItemDTO;
import com.mony.order.enums.OrderStatus;
import com.mony.order.exception.ResourceNotFoundException;
import com.mony.order.mapper.OrderItemMapper;
import com.mony.order.mapper.OrderMapper;
import com.mony.order.model.OrderItemModel;
import com.mony.order.model.OrderModel;
import com.mony.order.repository.OrderItemRepository;
import com.mony.order.repository.OrderRepository;
import com.mony.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        OrderModel orderModel = orderMapper.toModel(orderDTO);
        
        BigDecimal totalAmount = orderModel.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderModel.setTotalAmount(totalAmount);

        orderModel.getItems().forEach(item -> item.setOrder(orderModel));

        OrderModel savedOrder = orderRepository.save(orderModel);

        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        OrderModel orderModel = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + orderId));

        orderModel.setOrderDate(orderDTO.orderDate());
        orderModel.setTotalAmount(orderDTO.totalAmount());
        orderModel.setStatus(orderDTO.status());
        orderModel.setCustomerId(orderDTO.customerId());

        orderModel.getItems().clear();

        List<OrderItemModel> updatedItems = orderDTO.items().stream()
                .map(itemDTO -> {
                    OrderItemModel item = new OrderItemModel();
                    item.setProductName(itemDTO.productName());
                    item.setQuantity(itemDTO.quantity());
                    item.setPrice(itemDTO.price());
                    item.setOrder(orderModel);
                    return item;
                })
                .collect(Collectors.toList());

        orderModel.getItems().addAll(updatedItems); // Adicionar os novos itens à lista existente

        OrderModel updatedOrder = orderRepository.save(orderModel);

        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Optional<OrderModel> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido não encontrado com o ID: " + orderId);
        }
        return OrderMapper.toDTO(orderOptional.get());
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderModel> orderModels = orderRepository.findAll();

        return OrderMapper.toDTOList(orderModels);
    }
}