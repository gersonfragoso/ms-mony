package com.mony.account.mapper;


import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.UserRegisterDTO;
import com.mony.account.model.UserModel;

public class UserMapper {


    public static UserDTO toDTO(UserModel user) {
        return new UserDTO(
                user.getNome(),
                user.getCpf(),
                user.getEmail()
        );
    }
    public static UserModel toEntity(UserRegisterDTO userRequest) {
        return new UserModel(
                null,  // ID será gerado automaticamente pelo banco
                userRequest.name(),
                userRequest.cpf(),
                userRequest.email(),
                userRequest.password()
        );
    }
}
