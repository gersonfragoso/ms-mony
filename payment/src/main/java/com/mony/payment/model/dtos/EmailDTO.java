package com.mony.payment.model.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record EmailDTO(UUID paymentId,
                       String orderCode,
                       UUID userId,
                       String nameUser,
                       String emailTo,
                       BigDecimal value,
                       String paymentStatus)
{
}
