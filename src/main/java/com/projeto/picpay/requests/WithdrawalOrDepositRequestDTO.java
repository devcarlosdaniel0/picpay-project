package com.projeto.picpay.requests;

import com.projeto.picpay.domain.Wallet;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawalOrDepositRequestDTO(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal value,

        @NotNull
        Long walletID
) {
}
