package com.mony.payment.model.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentWriteDTO(@NotBlank String orderCode, //ATENÇÃO
                              @NotNull @Positive BigDecimal value, //ATENÇÃO
                              @NotBlank @Size(min = 11, max = 11) String cpf,
                              @NotBlank String nameCard,
                              @NotBlank @Size(min = 13, max = 19) String numberCard,
                              @NotBlank @Size(max = 5, min = 5) String dueDate,
                              @NotBlank @Size(max = 3, min = 3) String code,
                              @NotNull UUID userId, //ATENÇÃO
                              @NotBlank String nameUser, //ATENÇÃO
                              @NotBlank @Email String email)//ATENÇÃO

{
}
