package com.projeto.picpay.util.Wallet;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.domain.WalletType;

import java.math.BigDecimal;

public class WalletCreator {
    public static Wallet createWalletToBeSaved() {
        return Wallet.builder()
                .fullName("carlos")
                .cpfCnpj("123")
                .email("carlos@gmail.com")
                .password("321")
                .balance(BigDecimal.valueOf(10))
                .walletType(WalletType.USER)
                .build();
    }

    public static Wallet createValidWallet() {
        return Wallet.builder()
                .id(1L)
                .fullName("carlos")
                .cpfCnpj("123")
                .email("carlos@gmail.com")
                .password("321")
                .balance(BigDecimal.valueOf(10))
                .walletType(WalletType.USER)
                .build();
    }

    public static Wallet createValidUpdatedWallet() {
        return Wallet.builder()
                .id(1L)
                .fullName("carlos 2")
                .cpfCnpj("123")
                .email("carlos@gmail.com")
                .password("321")
                .balance(BigDecimal.valueOf(10))
                .walletType(WalletType.USER)
                .build();
    }
}
