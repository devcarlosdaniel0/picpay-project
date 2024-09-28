package com.projeto.picpay.service;

import com.projeto.picpay.domain.Transfer;
import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.exception.InsufficientBalanceException;
import com.projeto.picpay.exception.TransferNotAllowedForWalletTypeException;
import com.projeto.picpay.exception.TransferNotAuthorizedException;
import com.projeto.picpay.mapper.TransferMapper;
import com.projeto.picpay.repository.TransferRepository;
import com.projeto.picpay.requests.TransferPostRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final WalletService walletService;

    @Transactional
    public Transfer transfer(TransferPostRequestBody transferPostRequestBody) {
        Wallet sender = walletService.findByIdOrThrowWalletIdNotFoundException(transferPostRequestBody.senderID());
        Wallet reciver = walletService.findByIdOrThrowWalletIdNotFoundException(transferPostRequestBody.reciverID());

        validateTransfer(transferPostRequestBody, sender);

        sender.setBalance(sender.getBalance().subtract(transferPostRequestBody.value()));
        reciver.setBalance(reciver.getBalance().add(transferPostRequestBody.value()));

        Transfer transfer = TransferMapper.INSTANCE.toTransfer(transferPostRequestBody);
        transfer.setSender(sender);
        transfer.setReciver(reciver);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transfer));

        return transferRepository.save(transfer);
    }

    private void validateTransfer(TransferPostRequestBody transferPostRequestBody, Wallet sender) {
        if (!walletService.isTransferAllowedForWalletType(sender)) {
            throw new TransferNotAllowedForWalletTypeException();
        }

        if (!walletService.isSenderBalanceEqualOrGreaterThan(sender, transferPostRequestBody.value())) {
            throw new InsufficientBalanceException();
        }

        if (!authorizationService.isAuthorized(transferPostRequestBody)) {
            throw new TransferNotAuthorizedException();
        }
    }
}
