package com.projeto.picpay.util.Wallet;

import com.projeto.picpay.requests.WithdrawalOrDepositResponse;

import java.math.BigDecimal;

public class DepositResponseCreator {
    public static WithdrawalOrDepositResponse createDepositResponse() {
        BigDecimal value = RequestDTOCreator.createRequestDTO().value();

        return WithdrawalOrDepositResponse.builder()
                .value(value)
                .walletID(RequestDTOCreator.createRequestDTO().walletID())
                .balance(WalletCreator.createValidWallet().getBalance().add(value))
                .build();
    }
}
