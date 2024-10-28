package com.mony.account.controller;

import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.UserRequestDTO;
import com.mony.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint to create a user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestDTO user) {
        return userService.createUser(user)
                .orElse(ResponseEntity.badRequest().build());
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<UserDTO> userLogin(@RequestParam String email, @RequestParam String password) {
        return userService.userLogin(email, password)
                .orElse(ResponseEntity.badRequest().build());
    }

    // Endpoint to find all users (implement pagination)
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers(@RequestParam int page, @RequestParam int size) {
        List<UserDTO> users = userService.findAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    // Endpoint to delete a user by UUID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to update a user by UUID
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO)
                .orElse(ResponseEntity.notFound().build());
    }
}

