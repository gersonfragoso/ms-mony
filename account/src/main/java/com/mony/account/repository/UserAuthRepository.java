package com.mony.account.repository;

import com.mony.account.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    /**
     * Encontra uma autenticação de usuário ativa (não utilizada e não expirada) pelo UUID do usuário.
     * @param userId o UUID do usuário
     * @param currentTime o tempo atual para validar expiração
     * @return um UserAuth ativo ou null se não encontrado
     */
    UserAuth findFirstByUserIdAndUsedFalseAndExpirationTimeAfterOrderByExpirationTimeDesc(UUID userId, LocalDateTime currentTime);

    /**
     * Encontra uma autenticação de usuário ativa (não utilizada e não expirada) pelo código OTP.
     * @param otpCode o código OTP
     * @param currentTime o tempo atual para validar expiração
     * @return um UserAuth ativo ou null se não encontrado
     */
    UserAuth findFirstByOtpCodeAndUsedFalseAndExpirationTimeAfter(String otpCode, LocalDateTime currentTime);
}
