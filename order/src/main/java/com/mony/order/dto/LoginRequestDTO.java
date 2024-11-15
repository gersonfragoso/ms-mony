package com.mony.order.dto;

import lombok.Getter;
import lombok.Setter;

//réplica do LoginRequestDTO de account.
@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;
}
