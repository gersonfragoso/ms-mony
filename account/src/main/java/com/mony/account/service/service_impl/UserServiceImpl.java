package com.mony.account.service.service_impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mony.account.dto.UserDTO;
import com.mony.account.dto.request_dto.LoginRequestDTO;
import com.mony.account.dto.request_dto.UserUpdateDTO;
import com.mony.account.exceptions.CpfAlreadyInUseException;
import com.mony.account.exceptions.EmailAlreadyInUseException;
import com.mony.account.exceptions.OtpExpiredOrInvalidException;
import com.mony.account.exceptions.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
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
    private OtpService otpService;

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

    public String initiateLogin(LoginRequestDTO loginRequestDTO) {
        UserModel user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("Credenciais inválidas");
        }

        // Gera o OTP e o envia ao usuário
        String otp = otpService.generateOtp(user.getId());
        log.info("OTP gerado: {}", otp);

        // Simulação de envio do OTP (e-mail ou SMS pode ser integrado aqui)
        // emailService.sendOtp(user.getEmail(), otp);

        return "OTP enviado com sucesso.";
    }


    public String validateOtpAndGenerateToken(String otpCode) {
        LocalDateTime now = LocalDateTime.now();

        // Obtém o registro ativo e não expirado do OTP mais recente
        UserAuth userAuth = userAuthRepository.findFirstByOtpCodeAndUsedFalseAndExpirationTimeAfter(otpCode, now);

        if (userAuth == null) {
            throw new OtpExpiredOrInvalidException("OTP inválido ou expirado");
        }

        // Verifica se o OTP é o mais recente para o usuário
        UserAuth latestOtp = userAuthRepository.findFirstByUserIdAndUsedFalseAndExpirationTimeAfterOrderByExpirationTimeDesc(userAuth.getUserId(),now);

        // Se o OTP passado não for o mais recente, é considerado inválido
        if (!userAuth.getOtpCode().equals(latestOtp.getOtpCode())) {
            throw new OtpExpiredOrInvalidException("Este OTP não é mais válido. Um novo OTP foi gerado.");
        }

        // Marca o OTP como usado
        userAuth.markAsUsed();
        userAuthRepository.save(userAuth);

        // Obtém o usuário associado ao OTP
        UserModel user = userRepository.findById(userAuth.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Gera e retorna o token JWT
        return tokenService.generateToken(user, otpCode);
    }





    public void deleteUser(String token) {
        // Extrair o userId do token
        String userId = extractUserIdFromToken(token);
        // Buscar o usuário pelo userId
        UserModel user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        // Excluir o usuário
        userRepository.delete(user);
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
    public String extractUserOtpFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("solutis-squad1")
                    .build()
                    .verify(token)
                    .getClaim("otp")
                    .asString();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado", e);
        }
    }


    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException("Email já está em uso");
        }
    }
    public UserDTO detailUser(String token) {
        // Extrair o userId do token
        String userId = extractUserIdFromToken(token);

        // Buscar o usuário no repositório
        UserModel user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Converter e retornar como DTO
        return UserMapper.toDTO(user);
    }

    private void validateCpf(String cpf) {
        if (userRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyInUseException("CPF já está em uso");
        }
    }

}
