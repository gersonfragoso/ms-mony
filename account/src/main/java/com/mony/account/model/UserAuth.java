package com.mony.account.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@ToString
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID userId;

    private String otpCode;

    private Boolean used = false;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    public UserAuth(UUID userId, String otpCode, Boolean used) {
        this.userId = userId;
        this.otpCode = otpCode;
        this.used = false;
    }

    public void updateOtpCode(String newOtpCode) {
        this.otpCode = newOtpCode;
        this.used = false;
    }

    public void markAsUsed() {
        this.used = true;
    }
}
