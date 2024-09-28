package com.projeto.picpay.mapper;

import com.projeto.picpay.domain.Transfer;
import com.projeto.picpay.requests.TransferPostRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    Transfer toTransfer(TransferPostRequestBody transferPostRequestBody);
}
