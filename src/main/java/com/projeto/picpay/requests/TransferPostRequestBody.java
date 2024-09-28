package com.projeto.picpay.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferPostRequestBody(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal value,
        @NotNull
        Long senderID,
        @NotNull
        Long reciverID
) {}
