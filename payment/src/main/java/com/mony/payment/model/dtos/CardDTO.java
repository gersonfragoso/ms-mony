package com.mony.payment.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CardDTO
    (@NotBlank String nameCard,
    @NotBlank @Size(min = 13, max = 19) String numberCard,
    @NotBlank @Size(max = 5, min = 5) String dueDate,
    @NotBlank @Size(max = 3, min = 3) String code)
{
}
