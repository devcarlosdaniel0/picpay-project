package com.projeto.picpay.requests;

import com.projeto.picpay.domain.WalletType;

import java.math.BigDecimal;

public record WalletPostRequestBody(
        String fullName,
        String cpfCnpj,
        String email,
        String password,
        BigDecimal balance,
        WalletType walletType
)
{}
