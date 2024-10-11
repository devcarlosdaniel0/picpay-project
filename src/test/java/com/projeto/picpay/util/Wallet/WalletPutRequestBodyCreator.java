package com.projeto.picpay.util.Wallet;

import com.projeto.picpay.requests.WalletPutRequestBody;

public class WalletPutRequestBodyCreator {
    public static WalletPutRequestBody createWalletPutRequestBody() {
        return WalletPutRequestBody.builder()
                .id(WalletCreator.createValidUpdatedWallet().getId())
                .fullName(WalletCreator.createValidUpdatedWallet().getFullName())
                .cpfCnpj(WalletCreator.createValidUpdatedWallet().getCpfCnpj())
                .email(WalletCreator.createValidUpdatedWallet().getEmail())
                .password(WalletCreator.createValidUpdatedWallet().getPassword())
                .balance(WalletCreator.createValidUpdatedWallet().getBalance())
                .walletType(WalletCreator.createValidUpdatedWallet().getWalletType())
                .build();
    }
}
