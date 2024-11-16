package com.mony.order.controller;

import com.mony.order.dto.OrderDTO;
import com.mony.order.dto.UserInfoDTO;
import com.mony.order.exception.ResourceNotFoundException;
import com.mony.order.integration.JwtService;
import com.mony.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderService orderService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.orderService = orderService;
    }

    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@Valid @RequestBody OrderDTO orderDTO, @RequestParam UUID userId) {
        return orderService.createOrder(orderDTO, userId);
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@Valid @RequestBody OrderDTO orderDTO, @RequestHeader String token) {
        // Verifica se o token está expirado
        if (jwtService.isTokenExpired(token)) {
            throw new ResourceNotFoundException("Token expirado.");
        }

        try {
            // Extrai as informações do token
            UserInfoDTO userInfoDTO = jwtService.extractUserInfo(token);

            // Verifica se o userId foi extraído corretamente
            if (userInfoDTO.getUserId() == null) {
                throw new ResourceNotFoundException("User ID não encontrado no token.");
            }

            // Chama o serviço para criar o pedido
            orderDTO.setOrderDate(LocalDate.now());
            orderDTO.setCustomerId(userInfoDTO.getUserId());
            return orderService.createOrder(orderDTO);

        } catch (IllegalArgumentException e) {
            // Lidar com o caso de erro ao converter userId para UUID
            throw new ResourceNotFoundException("User ID inválido no token.");
        } catch (Exception e) {
            // Captura outras exceções e retorna uma mensagem genérica
            throw new ResourceNotFoundException("Erro ao processar o token.");
        }
    }

    @PutMapping("/{orderId}")
    public OrderDTO updateOrder(@PathVariable UUID orderId, @Valid @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderId, orderDTO);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Pedido deletado com sucesso!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado!");
        }
    }


}