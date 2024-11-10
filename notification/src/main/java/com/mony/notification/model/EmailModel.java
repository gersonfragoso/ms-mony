package com.mony.notification.model;

import com.mony.notification.model.enums.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="emails")
@Data
public class EmailModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID emailId;

    @NotBlank
    private String subject;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;

    @Email
    @NotBlank
    @Size(max = 255)
    private String sender;

    @Email
    @NotBlank
    @Size(max = 255)
    private String receiver;

    private LocalDateTime emailSentDate;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;


}
