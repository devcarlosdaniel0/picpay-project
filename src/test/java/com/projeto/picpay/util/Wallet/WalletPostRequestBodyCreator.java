package com.projeto.picpay.util.Wallet;

import com.projeto.picpay.requests.WalletPostRequestBody;

public class WalletPostRequestBodyCreator {
    public static WalletPostRequestBody createWalletPostRequestBody() {
        return WalletPostRequestBody.builder()
                .fullName(WalletCreator.createWalletToBeSaved().getFullName())
                .cpfCnpj(WalletCreator.createWalletToBeSaved().getCpfCnpj())
                .email(WalletCreator.createWalletToBeSaved().getEmail())
                .password(WalletCreator.createWalletToBeSaved().getPassword())
                .balance(WalletCreator.createWalletToBeSaved().getBalance())
                .walletType(WalletCreator.createWalletToBeSaved().getWalletType())
                .build();
    }
}
