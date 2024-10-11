package com.projeto.picpay.service;

import com.projeto.picpay.domain.Transfer;
import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.exception.*;
import com.projeto.picpay.mapper.TransferMapper;
import com.projeto.picpay.repository.TransferRepository;
import com.projeto.picpay.requests.TransferPostRequestBody;
import com.projeto.picpay.requests.TransferPutRequestBody;
import com.projeto.picpay.requests.TransferRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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

    private void validateTransfer(TransferRequestBody transferRequestBody, Wallet sender) {
        if (sender.getId().equals(transferRequestBody.reciverID())) {
            throw new SenderEqualsToReciverIdException();
        }

        if (!walletService.isTransferAllowedForWalletType(sender)) {
            throw new TransferNotAllowedForWalletTypeException();
        }

        if (!walletService.isSenderBalanceEqualOrGreaterThan(sender, transferRequestBody.value())) {
            throw new InsufficientBalanceException();
        }

        if (!authorizationService.isAuthorized(transferRequestBody)) {
            throw new TransferNotAuthorizedException();
        }
    }

    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    public Transfer findByIdOrThrowsTransactionIdNotFoundException(UUID id) {
        return transferRepository.findById(id).orElseThrow(() -> new TransactionIdNotFoundException("Transaction ID not found"));
    }

    @Transactional
    public void delete(UUID id) {
        transferRepository.delete(findByIdOrThrowsTransactionIdNotFoundException(id));
    }

    @Transactional
    public void replace(TransferPutRequestBody transferPutRequestBody) {
        Transfer transferDB = findByIdOrThrowsTransactionIdNotFoundException(transferPutRequestBody.id());
        Wallet sender = walletService.findByIdOrThrowWalletIdNotFoundException(transferPutRequestBody.senderID());
        Wallet reciver = walletService.findByIdOrThrowWalletIdNotFoundException(transferPutRequestBody.reciverID());

        validateTransfer(transferPutRequestBody, sender);

        sender.setBalance(sender.getBalance().subtract(transferPutRequestBody.value()));
        reciver.setBalance(reciver.getBalance().add(transferPutRequestBody.value()));

        Transfer transferToReplace = TransferMapper.INSTANCE.toTransfer(transferPutRequestBody);
        transferToReplace.setId(transferDB.getId());
        transferToReplace.setSender(sender);
        transferToReplace.setReciver(reciver);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transferToReplace));

        transferRepository.save(transferToReplace);
    }
}
