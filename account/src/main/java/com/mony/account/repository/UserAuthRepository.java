package com.mony.account.repository;

import com.mony.account.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    /**
     * Encontra uma autenticação de usuário ativa (não utilizada) pelo UUID do usuário.
     * @param userId o UUID do usuário
     * @return um Optional contendo UserAuth se encontrado, vazio caso contrário
     */
    Optional<UserAuth> findActiveByUserId(UUID userId);

    /**
     * Encontra uma autenticação de usuário ativa (não utilizada) pelo código OTP.
     * @param otpCode o código OTP
     * @return um Optional contendo UserAuth se encontrado, vazio caso contrário
     */
    Optional<UserAuth> findActiveByOtpCode(String otpCode);

}