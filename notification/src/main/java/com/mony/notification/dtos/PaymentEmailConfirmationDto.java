package com.mony.notification.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentEmailConfirmationDto(@JsonProperty("paymentId") UUID paymentId,
                                          @JsonProperty("orderCode") @JsonAlias({"orderNumber", "order_code"}) String orderCode,
                                          @JsonProperty("userId") UUID userId,
                                          @JsonProperty("nameUser")String nameUser,
                                          @JsonAlias({"email", "emailTo"})String emailTo,
                                          @JsonProperty("value") BigDecimal value,
                                          @JsonProperty("status") String paymentStatus) {
}
