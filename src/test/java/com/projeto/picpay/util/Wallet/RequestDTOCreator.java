package com.projeto.picpay.util.Wallet;

import com.projeto.picpay.requests.WithdrawalOrDepositRequestDTO;

import java.math.BigDecimal;

public class RequestDTOCreator {
    public static WithdrawalOrDepositRequestDTO createRequestDTO() {
        return WithdrawalOrDepositRequestDTO.builder()
                .value(BigDecimal.valueOf(5))
                .walletID(1L)
                .build();
    }
}
