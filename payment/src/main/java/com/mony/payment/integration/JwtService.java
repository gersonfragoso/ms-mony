package com.mony.payment.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mony.payment.model.dtos.UserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    /*public String validateToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String payload = decodedJWT.getPayload();
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

        return "Payload: " + decodedPayload;
    }*/

    public UserInfoDTO extractUserInfo(String token) {
        // Decodificando o token JWT
        DecodedJWT decodedJWT = JWT.decode(token);

        // Recuperando as informações do token
        String email = decodedJWT.getSubject();  // "sub" (email)
        String userId = decodedJWT.getClaim("userId").asString();  // "userId"
        String name = decodedJWT.getClaim("username").asString();  // "username" (nome)
        String cpf = decodedJWT.getClaim("cpf").asString();
        // Criando o DTO e preenchendo com as informações extraídas do token
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(UUID.fromString(userId));  // Convertendo o userId para UUID
        userInfoDTO.setEmail(email);
        userInfoDTO.setName(name);
        userInfoDTO.setCpf(cpf);

        return userInfoDTO;
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = JWT.decode(token); // Decodifica o token
        long expTimestamp = decodedJWT.getExpiresAt().getTime(); // Recupera o valor de "exp" em milissegundos
        long currentTimestamp = Instant.now().toEpochMilli(); // Recupera o timestamp atual em milissegundos
        // Compara a data de expiração com a data atual
        return expTimestamp < currentTimestamp; // Retorna verdadeiro se o token estiver expirado
    }


}
