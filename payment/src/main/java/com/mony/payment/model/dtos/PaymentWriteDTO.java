package com.mony.payment.model.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentWriteDTO(@NotBlank String orderNumber,
                              @NotNull @Positive BigDecimal value,
                              @NotBlank @Size(min = 11, max = 11) String cpf,
                              @NotBlank String nameCard,
                              @NotBlank @Size(min = 13, max = 19) String number,
                              @NotBlank @Size(max = 5, min = 5) String dueDate,
                              @NotBlank @Size(max = 3, min = 3) String code,
                              @NotNull UUID userId,
                              @NotBlank String nameUser,
                              @NotBlank @Email String email)

{
}
