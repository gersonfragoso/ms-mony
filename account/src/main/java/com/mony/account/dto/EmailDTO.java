package com.mony.account.dto;

import java.util.UUID;

public record EmailDTO (
        UUID userId,
        String email_recipient,
        String codeAuth2FA){
}
