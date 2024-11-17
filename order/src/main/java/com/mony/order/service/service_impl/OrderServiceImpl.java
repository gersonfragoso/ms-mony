package com.mony.order.service.service_impl;

import com.mony.order.dto.OrderDTO;
import com.mony.order.enums.OrderStatus;
import com.mony.order.exception.ResourceNotFoundException;
import com.mony.order.mapper.OrderMapper;
import com.mony.order.model.OrderItemModel;
import com.mony.order.model.OrderModel;
import com.mony.order.repository.OrderItemRepository;
import com.mony.order.repository.OrderRepository;
import com.mony.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /*
    @Autowired
    private AccountFeignClient accountFeignClient;
*/
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Converte o DTO para modelo (entidade)
        OrderModel orderModel = OrderMapper.toModel(orderDTO);
        orderModel.setStatus(OrderStatus.PENDING);

        // Calcula o total do pedido
        BigDecimal totalAmount = orderModel.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderModel.setTotalAmount(totalAmount);

        // Garante que todos os itens estão associados ao pedido
        orderModel.getItems().forEach(item -> item.setOrder(orderModel));

        // Persiste o pedido e seus itens
        OrderModel savedOrder = orderRepository.save(orderModel); // Isso vai salvar o pedido e seus itens devido ao Cascade

        return OrderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(UUID orderId, OrderDTO orderDTO) {
        OrderModel orderModel = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + orderId));

        orderModel.setOrderDate(orderDTO.getOrderDate());
        orderModel.setTotalAmount(orderDTO.getTotalAmount());
        orderModel.setCustomerId(orderDTO.getCustomerId());
        if(orderDTO.getStatus().equalsIgnoreCase(OrderStatus.PENDING.toString()))
            orderModel.setStatus(OrderStatus.PENDING);
        else if(orderDTO.getStatus().equalsIgnoreCase(OrderStatus.CANCELLED.toString()))
            orderModel.setStatus(OrderStatus.CANCELLED);
        else if(orderDTO.getStatus().equalsIgnoreCase(OrderStatus.COMPLETED.toString()))
            orderModel.setStatus(OrderStatus.COMPLETED);
        else if (orderDTO.getStatus().equalsIgnoreCase(OrderStatus.PROCESSING.toString()))

        orderModel.getItems().clear();

        List<OrderItemModel> updatedItems = orderDTO.getItems().stream()
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

        return OrderMapper.toDTO(updatedOrder);
    }

    @Override
    public OrderDTO getOrderById(UUID orderId) {
        Optional<OrderModel> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Pedido não encontrado com o ID: " + orderId);
        }
        return OrderMapper.toDTO(orderOptional.get());
    }

    public void deleteOrder(UUID orderId) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID " + orderId));

        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderModel> orderModels = orderRepository.findAll();

        return OrderMapper.toDTOList(orderModels);
    }
}