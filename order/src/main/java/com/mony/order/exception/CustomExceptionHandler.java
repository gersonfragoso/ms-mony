package com.mony.order.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    // Exceção para token expirado
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredToken(ExpiredJwtException ex) {
        // Retorna uma mensagem personalizada para quando o token estiver expirado
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token expirado. Por favor, faça login novamente.");
    }

    // Exceção para token inválido (malformado ou outro erro de JWT)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleInvalidToken(JwtException ex) {
        // Retorna uma mensagem personalizada para quando o token for inválido
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido. Por favor, forneça um token válido.");
    }
}
