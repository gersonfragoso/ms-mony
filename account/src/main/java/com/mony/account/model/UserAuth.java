package com.mony.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID userId;

    private String otpCode;

    private Boolean isUsed = false;

    public UserAuth(UUID userId, String otpCode, Boolean isUsed) {
        this.userId = userId;
        this.otpCode = otpCode;
        this.isUsed = false;
    }

    /**
     * Atualiza o código OTP e define isUsed como false para reutilização.
     * @param newOtpCode Novo código OTP a ser definido.
     */
    public void updateOtpCode(String newOtpCode) {
        this.otpCode = newOtpCode;
        this.isUsed = false;
    }

    /**
     * Marca o OTP como usado para evitar reuso.
     */
    public void markAsUsed() {
        this.isUsed = true;
    }
}
