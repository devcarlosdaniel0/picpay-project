package com.projeto.picpay.requests;

import com.projeto.picpay.domain.WalletType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WalletPutRequestBody(
        @NotNull
        Long id,
        @NotBlank
        String fullName,
        @NotBlank
        String cpfCnpj,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotNull
        BigDecimal balance,
        @NotNull
        WalletType walletType
)
{}
