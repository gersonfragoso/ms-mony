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
import java.util.Map;
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
    public ResponseEntity<String> initiateLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        String response = userService.initiateLogin(loginRequestDTO);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody Map<String, String> request) {
        String otpCode = request.get("otpCode");
        String jwtToken = userService.validateOtpAndGenerateToken(otpCode);
        return ResponseEntity.ok(jwtToken);
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
    public ResponseEntity<Void> updateUser(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody UserUpdateDTO userDTO) {
        // Extrair o token do cabeçalho
        String token = authorizationHeader.replace("Bearer ", "");
        // Atualizar o usuário
        userService.updateUser(token, userDTO);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrair o token do cabeçalho
        String token = authorizationHeader.replace("Bearer ", "");
        // Chamar o serviço para excluir o usuário
        userService.deleteUser(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/detail")
    public ResponseEntity<UserDTO> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        // Extrair o token do cabeçalho
        String token = authorizationHeader.replace("Bearer ", "");

        // Obter os detalhes do usuário
        UserDTO userDTO = userService.detailUser(token);

        // Retornar como resposta
        return ResponseEntity.ok(userDTO);
    }

}
