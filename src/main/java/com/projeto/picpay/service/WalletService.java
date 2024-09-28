package com.projeto.picpay.service;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.exception.WalletDataAlreadyExistsException;
import com.projeto.picpay.exception.WalletIdNotFoundException;
import com.projeto.picpay.mapper.WalletMapper;
import com.projeto.picpay.repository.WalletRepository;
import com.projeto.picpay.requests.WalletPostRequestBody;
import com.projeto.picpay.requests.WalletPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Método que cria um carteira e a salva.
     * @param walletPostRequestBody DTO de Wallet
     * @return Wallet para o Controller
     */
    @Transactional
    public Wallet createWallet(WalletPostRequestBody walletPostRequestBody) {
        Wallet wallet = WalletMapper.INSTANCE.toWallet(walletPostRequestBody);
        return saveWallet(wallet);
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

    /**
     * Faz o update da Wallet. Procura pelo ID da Wallet que está no banco de dados.
     * Então, faz um Mapping do DTO do Put. Essa será a nova Wallet que substituirá a antiga.
     * Setta o ID da nova wallet para a antiga para apenas mudar os dados e não criar uma nova.
     * @param walletPutRequestBody
     * @return void pois no Controller, o ResponseEntity é void. (NO_CONTENT)
     */
    @Transactional
    public void replace(WalletPutRequestBody walletPutRequestBody) {
        Wallet walletDB = findByIdOrThrowWalletIdNotFoundException(walletPutRequestBody.id());
        Wallet walletToReplace = WalletMapper.INSTANCE.toWallet(walletPutRequestBody);
        walletToReplace.setId(walletDB.getId());
        saveWallet(walletToReplace);
    }

    /**
     * Método privado para salvar uma carteira. Verifica a existência de CPF/CNPJ e email.
     * @param wallet A carteira a ser salva.
     * @return A carteira salva.
     */
    private Wallet saveWallet(Wallet wallet) {
        Optional<Wallet> walletDB = walletRepository.findByCpfCnpjOrEmail(wallet.getCpfCnpj(), wallet.getEmail());
        if (walletDB.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCpnj or Email already exists");
        }

        return walletRepository.save(wallet);
    }
}
