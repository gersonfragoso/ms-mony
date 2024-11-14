package com.mony.account.dto.request_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.*;

public record UserRegisterDTO(
        @NotBlank(message = "O nome não pode estar vazio.") String name,
        @NotBlank(message = "O CPF não pode estar vazio.") @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres.") String cpf,
        @NotBlank(message = "A senha não pode estar vazia.") String password,
        @NotBlank(message = "O email não pode estar vazio.") @Email(message = "O email deve ser válido.") String email
) {}
