package com.mony.notification.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentEmailConfirmationDto(UUID paymentId,
                                          String orderCode,
                                          UUID userId,
                                          String nameUser,
                                          String emailTo,
                                          BigDecimal value,
                                          String paymentStatus) {
}
