package com.projeto.picpay.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WithdrawalOrDepositRequestDTO(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal value,

        @NotNull
        Long walletID
) {
}
