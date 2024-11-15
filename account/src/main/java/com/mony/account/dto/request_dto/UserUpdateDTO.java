package com.mony.account.dto.request_dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateDTO(
        @NotNull UUID userId,
        String name,
        String email) {
}
