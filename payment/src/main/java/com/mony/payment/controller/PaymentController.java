package com.mony.payment.controller;

import com.mony.payment.model.dtos.PaymentReadDTO;
import com.mony.payment.model.dtos.PaymentWriteDTO;
import com.mony.payment.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import java.util.UUID;

@OpenAPIDefinition(info = @Info(title = "Mony - Payment API", version = "1.0", description = "API para o processamento de pagamentos"))
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /*URL PARA TESTE: http://localhost:8081/payments/id
    ATENÇÃO: necessário colocar header: KEY: id; value: seu UUID.
    * */
    @Operation(summary = "Obter pagamento por ID [HEADER]", description = "Recupera o pagamento utilizando o" +
            " ID fornecido via header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/id")
    public ResponseEntity<PaymentReadDTO> getPaymentById(@RequestHeader UUID id) {
        try {
            PaymentReadDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(payment); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 500 Internal Server Error
        }
    }

    /*Exemplo de URL para a requisição:
    http://localhost:8081/payments/id/UUID-AQUI
    * */
    @Operation(summary = "Obter pagamento por ID [URL PATH]", description = "Recupera o pagamento utilizando" +
            " o ID fornecido diretamente na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<PaymentReadDTO> getPaymentByIdInPath(@PathVariable UUID id) {
        try {
            PaymentReadDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(payment); // 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    /*URL para testar:
    http://localhost:8081/payments/cpf/SEUCPFAQUI?page=0&size=10
    * */
    @Operation(summary = "Obter pagamentos por CPF [URL PATH]", description = "Recupera os pagamentos associados" +
            " a um CPF específico fornecido via URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos do CPF encontrados com sucesso."),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - nenhum pagamento encontrado para o CPF informado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Page<PaymentReadDTO>> getPaymentsByCpfInPath(@PathVariable String cpf, Pageable pageable) {
        try {
            Page<PaymentReadDTO> payments = paymentService.getPaymentsByCpf(cpf, pageable);

            if (payments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
            }
            return ResponseEntity.status(HttpStatus.OK).body(payments); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /*exemplo de URL para o get abaixo:
     * http://localhost:8080/payments?page=0&size=10
     * Necessário ajustar no postman um Header: KEY: cpf , VALUE: o número do cpf pesquisado.
     * */
    @Operation(summary = "Obter pagamentos por CPF [HEADER]", description = "Recupera os pagamentos associados" +
            " ao CPF informado no header da requisição.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos do CPF encontrados com sucesso."),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - nenhum pagamento encontrado para o CPF."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping
    public ResponseEntity<Page<PaymentReadDTO>> getPaymentsByCpf(@RequestHeader String cpf, Pageable pageable) {
        try {
            Page<PaymentReadDTO> payments = paymentService.getPaymentsByCpf(cpf, pageable);

            if (payments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
            }
            return ResponseEntity.status(HttpStatus.OK).body(payments); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @Operation(summary = "Processar pagamento", description = "Processa um pagamento com as" +
            " informações fornecidas, requisitando API externa e gravando no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso. Obs: não necessariamente a operação" +
                    " de pagamento foi confirmada."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping
    public ResponseEntity<PaymentReadDTO> processPayment(@RequestBody @Valid PaymentWriteDTO payment) {
        try {
            PaymentReadDTO processedPayment = paymentService.processPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(processedPayment); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 500 Internal Server Error
        }
    }
}