package com.mony.account.controller;

import com.mony.account.dto.LoginResponseDTO;
import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.LoginRequestDTO;
import com.mony.account.dto.request_dto.UserUpdateDTO;
import com.mony.account.model.UserModel;
import com.mony.account.service.service_impl.UserServiceImpl;

import com.mony.infra.security.config.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;



    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers(@RequestParam int page, @RequestParam int size) {
        List<UserDTO> users = userService.findAllUsers(page, size);
        return ResponseEntity.ok(users);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok().body(userService.login(loginRequestDTO));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel data) {
        userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario" + data);
    }

    // Método para obter o e-mail do usuário logado
    private String getLoggedInUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PutMapping("/update")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok().body("Usuario atulizado!" + userUpdateDTO);
    }


    @DeleteMapping
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> deleteUser(@RequestHeader String cpf) {
        userService.deleteUser(cpf);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
