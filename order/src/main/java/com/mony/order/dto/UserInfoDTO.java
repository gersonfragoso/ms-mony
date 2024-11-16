package com.mony.order.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class UserInfoDTO {
    private UUID userId;
    private String name;
    private String email;
    private String cpf;
}
