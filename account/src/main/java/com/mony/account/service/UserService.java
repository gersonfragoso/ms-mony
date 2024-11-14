package com.mony.account.service;

import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.UserRegisterDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {

    Optional<ResponseEntity<UserDTO>> createUser(UserRegisterDTO user);
    Optional<ResponseEntity<UserDTO>> userLogin(String email, String password);
    List<UserDTO> findAllUsers(int page, int size);
    Optional<ResponseEntity<Void>> deleteUser(String cpf);
    ResponseEntity<UserDTO> updateUser(String loggedInUserEmail, UserDTO userDTO);
    boolean validateEmail(String email);
}