package com.mony.payment.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
//DTO criado apenas para recuperar informações do token de login!
@Getter
@Setter
public class UserInfoDTO {
    private UUID userId;
    private String name;
    private String email;
}
