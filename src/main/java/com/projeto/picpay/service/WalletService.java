package com.projeto.picpay.service;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.exception.WalletDataAlreadyExistsException;
import com.projeto.picpay.exception.WalletIdNotFoundException;
import com.projeto.picpay.mapper.WalletMapper;
import com.projeto.picpay.repository.WalletRepository;
import com.projeto.picpay.requests.WalletPostRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional
    public Wallet createWallet(WalletPostRequestBody walletPostRequestBody) {
        Optional<Wallet> walletDB = walletRepository.findByCpfCnpjOrEmail(walletPostRequestBody.cpfCnpj(), walletPostRequestBody.email());
        if (walletDB.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCpnj or Email already exists");
        }

        Wallet wallet = WalletMapper.INSTANCE.toWallet(walletPostRequestBody);
        return walletRepository.save(wallet);
    }

    public Wallet findByIdOrThrowWalletIdNotFoundException(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletIdNotFoundException("Wallet ID not found"));
    }

    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        walletRepository.delete(findByIdOrThrowWalletIdNotFoundException(id));
    }
}
