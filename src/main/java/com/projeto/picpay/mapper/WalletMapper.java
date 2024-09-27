package com.projeto.picpay.mapper;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.requests.WalletPostRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    Wallet toWallet(WalletPostRequestBody walletPostRequestBody);
}
