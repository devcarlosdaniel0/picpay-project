package com.projeto.picpay.requests;

import com.projeto.picpay.domain.Wallet;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WithdrawalOrDepositResponse(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal value,

        @NotNull
        Long walletID,

        @NotNull
        BigDecimal balance
) {
}
