package com.mony.account.service;

import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    Optional<ResponseEntity<UserDTO>> createUser(UserRequestDTO user);
    Optional<ResponseEntity<UserDTO>> userLogin(String email, String password);
    List<UserDTO> findAllUsers(int page, int size);
    Optional<ResponseEntity<Void>> deleteUser(UUID userId);
    Optional<ResponseEntity<UserDTO>> updateUser(UUID UserId,UserDTO userDTO);
    boolean validateEmail(String email);
}