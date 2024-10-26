package com.mony.account.service.service_impl;


import com.mony.account.dto.UserDTO;
import com.mony.account.mapper.UserMapper;
import com.mony.account.model.UserAuth;
import com.mony.account.model.UserModel;
import com.mony.account.repository.UserAuthRepository;
import com.mony.account.repository.UserRepository;
import com.mony.account.service.UserService;
import com.mony.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public Optional<ResponseEntity<UserDTO>> createUser(UserDTO user) {
        if (!validateEmail(user.email())){
            return Optional.of(ResponseEntity.badRequest().build());
        }
        UserModel userToSave = UserMapper.toEntity(user);
        userRepository.save(userToSave);
        return Optional.of(
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserMapper.toDTO(userToSave)));
    }

    @Override
    public Optional<ResponseEntity<UserDTO>> userLogin(String email, String password) {
        UserModel user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            return Optional.of(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } else {
            return Optional.of(ResponseEntity.ok(UserMapper.toDTO(user)));
        }
    }

    @Override
    public List<UserDTO> findAllUsers(int page, int size) {
        return List.of();
    }

    @Override
    public Optional<ResponseEntity<Void>> deleteUser(UUID userId) {
        return Optional.empty();
    }

    @Override
    public Optional<ResponseEntity<UserDTO>> updateUser(UUID UserId, UserDTO userDTO) {
        return Optional.empty();
    }

    @Override
    public boolean validateEmail(String email) {
        return false;
    }

    private String generateOTP(UUID userId){
        Optional<UserAuth> userAuth = userAuthRepository.findActiveByUserId(userId);
        String otpCode;
        if(userAuth.isPresent()){
            otpCode = userAuth.get().getOtpCode();
        }else {
            otpCode = SecurityUtil.generateOtpCode();
            userAuthRepository.save(new UserAuth(userId,otpCode,false));
        }
        return otpCode;
    }
}
