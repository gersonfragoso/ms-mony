package com.mony.account.mapper;


import com.mony.account.dto.UserDTO;
import com.mony.account.model.UserModel;

public class UserMapper {

    public static UserDTO toDTO(UserModel user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getNome(), user.getCpf(), user.getEmail());
    }
    public static UserModel toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        UserModel user = new UserModel();
        user.setNome(userDTO.nome());
        user.setCpf(userDTO.cpf());
        user.setEmail(userDTO.email());
        return user;
    }
}
