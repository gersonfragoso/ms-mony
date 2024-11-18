package com.mony.account.service.service_impl;

import com.mony.account.exceptions.OtpExpiredOrInvalidException;
import com.mony.account.model.UserAuth;
import com.mony.account.repository.UserAuthRepository;
import com.mony.infra.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OtpService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    public String generateOtp(UUID userId) {
        // Invalida qualquer OTP anterior associado ao usuário
        LocalDateTime now = LocalDateTime.now();
        UserAuth existingAuth = userAuthRepository.findFirstByUserIdAndUsedFalseAndExpirationTimeAfterOrderByExpirationTimeDesc(userId, now);
        if (existingAuth != null) {
            existingAuth.markAsUsed(); // Marca como usado
            userAuthRepository.save(existingAuth);
        }

        // Gera um novo OTP
        String otpCode = SecurityHelper.createOtpCode();

        // Salva o novo OTP com tempo de expiração
        UserAuth newAuth = new UserAuth(userId, otpCode, false);
        newAuth.setExpirationTime(now.plusMinutes(5)); // Expira em 5 minutos
        userAuthRepository.save(newAuth);

        return otpCode;
    }

    public boolean validateOtp(String otpCode) {
        LocalDateTime now = LocalDateTime.now();
        UserAuth userAuth = userAuthRepository.findFirstByOtpCodeAndUsedFalseAndExpirationTimeAfter(otpCode, now);

        if (userAuth == null) {
            throw new OtpExpiredOrInvalidException("OTP inválido ou expirado");
        }

        userAuth.markAsUsed(); // Marca como usado
        userAuthRepository.save(userAuth);
        return true;
    }
}

