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

import java.util.UUID;

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
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Page<PaymentReadDTO>> getPaymentsByCpfInPath(@PathVariable String cpf, Pageable pageable) {
        try {
            Page<PaymentReadDTO> payments = paymentService.getPaymentsByCpf(cpf, pageable);

            if (payments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /*exemplo de URL para o get abaixo:
    * http://localhost:8080/payments?page=0&size=10
    * Necessário ajustar no postman um Header: KEY: cpf , VALUE: o número do cpf pesquisado.
    * */

    @GetMapping
    public ResponseEntity<Page<PaymentReadDTO>> getPaymentsByCpf(@RequestHeader String cpf, Pageable pageable) {
        try{
            Page<PaymentReadDTO> payments = paymentService.getPaymentsByCpf(cpf, pageable);

            if (payments.isEmpty()) {
                // 204 No Content -> se não há pagamentos para o CPF fornecido
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(payments);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
    JSON PARA CRIAÇÃO POST
                {
              "orderCode": "123456789",
              "value": 150.75,
              "cpf": "12345678901",
              "nameCard": "João Silva",
              "numberCard": "1234567890123456",
              "dueDate": "12/25",
              "code": "123",
              "userId": "550e8400-e29b-41d4-a716-446655440000",
              "nameUser": "Maria Oliveira",
              "email": "maria.oliveira@example.com"
            }

     */
    @PostMapping
    public ResponseEntity<PaymentReadDTO> processPayment(@RequestBody @Valid PaymentWriteDTO payment) {
        try {
            PaymentReadDTO processedPayment = paymentService.processPayment(payment);
            // Retorna 201 Created -> pagamento processado com sucesso
            return ResponseEntity.status(HttpStatus.CREATED).body(processedPayment);
        } catch (IllegalArgumentException e) {
            // Retorna 400 Bad Request -> dados inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception e) {
            // Retorna 500 Internal Server Error -> qualquer outra exceção inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
