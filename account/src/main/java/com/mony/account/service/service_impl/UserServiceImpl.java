package com.mony.account.service.service_impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.LoginRequestDTO;
import com.mony.account.dto.request_dto.UserUpdateDTO;
import com.mony.account.mapper.UserMapper;
import com.mony.account.model.UserAuth;
import com.mony.account.model.UserModel;
import com.mony.account.repository.UserAuthRepository;
import com.mony.account.repository.UserRepository;
import com.mony.infra.security.config.TokenService;
import com.mony.infra.util.SecurityHelper;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    private String secretKey;



    public void createUser(UserModel user){
        validateEmail(user.getEmail());
        validateCpf(user.getCpf());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<UserDTO> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserModel> userPage = userRepository.findAll(pageable);

        return userPage.getContent().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        // Buscando o UserModel do banco de dados
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Usuário não encontrado"));

        // Criando o token de autenticação
        var token = new UsernamePasswordAuthenticationToken(email, password);
        var authentication = authenticationManager.authenticate(token);

        // Verificando a senha
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Se a senha for válida, gera o OTP
            //return generateOtp(user.getId());
            return tokenService.generateToken(user);
        } else {
            throw new AuthenticationCredentialsNotFoundException("Usuário ou senha inválidos");
        }
    }




    private String generateOtp(UUID id) {
        UserAuth userAuth = userAuthRepository.findActiveByUserId(id);
        String otpCode;
        if (userAuth != null) {
            otpCode = userAuth.getOtpCode();
        } else {
            otpCode = SecurityHelper.createOtpCode();
            userAuthRepository.save(new UserAuth(id, otpCode, false));
        }
        return otpCode;
    }


    public void deleteUser(String Cpf) {
        Optional<UserModel> user = userRepository.findByCpf(Cpf);
        if (userRepository.existsByCpf(Cpf)) {
            userRepository.deleteByCpf(Cpf);
            ResponseEntity.noContent().build();
            return;
        }
        ResponseEntity.notFound().build();
    }

    public void updateUser(String token, UserUpdateDTO userDTO) {
        // Extrair o userId do token
        String userId = extractUserIdFromToken(token);

        // Buscar o usuário no banco de dados
        UserModel user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        // Atualizar os dados fornecidos no DTO
        if (StringUtils.hasText(userDTO.name())) {
            user.setNome(userDTO.name());
        }
        if (StringUtils.hasText(userDTO.email())) {
            validateEmail(userDTO.email());
            user.setEmail(userDTO.email());
        }

        // Salvar as alterações
        log.info(user.getNome());
        log.info(userDTO.name());
        userRepository.save(user);
    }

    public String extractUserIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("solutis-squad1")
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asString();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }

    private void validateEmail(String email) {
        Optional<UserModel> emailsSaved = userRepository.findByEmail(email);
        if (emailsSaved.isPresent()) {
            throw new EntityExistsException("Email já está em uso");
        }
    }
    private UserDTO detailUser(UUID userId){
        UserModel user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return UserMapper.toDTO(user);
    }

    private void validateCpf(String cpf){
        boolean cpfSalvo = userRepository.existsByCpf(cpf);
        if (cpfSalvo){
            throw new EntityExistsException("Cpf em uso");
        }
    }
    public Boolean validateOTP(String otpCode){

        return userAuthRepository.findActiveByOtpCode(otpCode) != null;
    }

    public UserAuth getOtpEntity(String otpCode) {
        return userAuthRepository.findActiveByOtpCode(otpCode);
    }

}
