package com.mony.order.controller;

import com.mony.order.dto.OrderDTO;
import com.mony.order.dto.UserInfoDTO;
import com.mony.order.exception.ResourceNotFoundException;
import com.mony.order.integration.JwtService;
import com.mony.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Pedidos", description = "Gerenciamento de Pedidos")
public class OrderController {

    private final OrderService orderService;

    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderService orderService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Criar um novo pedido",
            description = "Cria um novo pedido, fornecendo itens, quantidades e preços. Retorna o pedido criado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@Valid @RequestBody OrderDTO orderDTO, @RequestHeader String token) {
        try {
            if (jwtService.isTokenExpired(token)) {
                throw new ResourceNotFoundException("Token expirado.");
            }

            UserInfoDTO userInfoDTO = jwtService.extractUserInfo(token);

            if (userInfoDTO.getUserId() == null) {
                throw new ResourceNotFoundException("User ID não encontrado no token.");
            }

            orderDTO.setOrderDate(LocalDate.now());
            orderDTO.setCustomerId(userInfoDTO.getUserId());
            return orderService.createOrder(orderDTO);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro ao processar o token ou token inválido.");
        }
    }

    @Operation(summary = "Alterar status pedido", description = "Altera o status de um pedido específico do sistema,após pagar.")
    @Parameter(name = "id", description = "ID do pedido a ser alterado", required = true)
    @PutMapping("/{orderId}")
    public OrderDTO updateOrder(@PathVariable UUID orderId, @Valid @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderId, orderDTO);
    }

    @Operation(
            summary = "Alterar pedido no carrinho",
            description = "Altera os itens de um pedido existente no carrinho, incluindo quantidades e preços.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID do pedido a ser alterado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido alterado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos no pedido",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/cart/{orderId}")
    public OrderDTO updateOrderCart(@PathVariable UUID orderId, @Valid @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrderCart(orderId, orderDTO);
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna os detalhes de um pedido específico, fornecido seu ID.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID do pedido a ser consultado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    @Operation(
            summary = "Listar todos os pedidos",
            description = "Retorna uma lista de todos os pedidos no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pedidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDTO.class, type = "array"))),
                    @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado")
            }
    )
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(
            summary = "Deletar pedido",
            description = "Remove um pedido específico do sistema.",
            parameters = {
                    @Parameter(name = "orderId", description = "ID do pedido a ser deletado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
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