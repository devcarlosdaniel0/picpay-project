package com.projeto.picpay.service;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.domain.WalletType;
import com.projeto.picpay.exception.WalletDataAlreadyExistsException;
import com.projeto.picpay.exception.WalletIdNotFoundException;
import com.projeto.picpay.mapper.WalletMapper;
import com.projeto.picpay.repository.WalletRepository;
import com.projeto.picpay.requests.WalletPostRequestBody;
import com.projeto.picpay.requests.WalletPutRequestBody;
import com.projeto.picpay.requests.WithdrawalOrDepositRequestDTO;
import com.projeto.picpay.requests.WithdrawalOrDepositResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Método que cria um carteira e a salva. Verifica a existência de CPF/CNPJ e email.
     * @param walletPostRequestBody DTO de Wallet
     * @return Wallet para o Controller
     */
    @Transactional
    public Wallet createWallet(WalletPostRequestBody walletPostRequestBody) {
        Optional<Wallet> walletDB = walletRepository.findByCpfCnpjOrEmail(walletPostRequestBody.cpfCnpj(), walletPostRequestBody.email());
        if (walletDB.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCpnj or Email already exists");
        }

        Wallet wallet = WalletMapper.INSTANCE.toWallet(walletPostRequestBody);
        return walletRepository.save(wallet);
    }

    public WithdrawalOrDepositResponse deposit(WithdrawalOrDepositRequestDTO requestDTO) {
        Wallet walletDB = findByIdOrThrowWalletIdNotFoundException(requestDTO.walletID());

        walletDB.setBalance(walletDB.getBalance().add(requestDTO.value()));

        return saveWalletAndCreateResponse(requestDTO, walletDB);
    }

    public WithdrawalOrDepositResponse withdrawal(WithdrawalOrDepositRequestDTO requestDTO) {
        Wallet walletDB = findByIdOrThrowWalletIdNotFoundException(requestDTO.walletID());

        walletDB.setBalance(walletDB.getBalance().subtract(requestDTO.value()));

        return saveWalletAndCreateResponse(requestDTO, walletDB);
    }

    private WithdrawalOrDepositResponse saveWalletAndCreateResponse(WithdrawalOrDepositRequestDTO requestDTO, Wallet walletDB) {
        walletRepository.save(walletDB);

        WithdrawalOrDepositResponse response = WithdrawalOrDepositResponse.builder()
                .balance(walletDB.getBalance())
                .value(requestDTO.value())
                .walletID(requestDTO.walletID())
                .build();

        return response;
    }

    /**
     * Valida se o tipo de usuário é permitido fazer transferências
     * @param wallet
     * @return true se o tipo for usuário
     */
    public boolean isTransferAllowedForWalletType(Wallet wallet) {
        return wallet.getWalletType().equals(WalletType.USER);
    }

    /**
     * Valida o saldo para ver se é suficiente
     * @param sender
     * @param value
     * @return
     */
    public boolean isSenderBalanceEqualOrGreaterThan(Wallet sender, BigDecimal value) {
        return sender.getBalance().compareTo(value) >= 0;
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
     * return void pois no Controller, o ResponseEntity é void. (NO_CONTENT)
     */
    @Transactional
    public void replace(WalletPutRequestBody walletPutRequestBody) {
        Wallet walletDB = findByIdOrThrowWalletIdNotFoundException(walletPutRequestBody.id());
        Wallet walletToReplace = WalletMapper.INSTANCE.toWallet(walletPutRequestBody);
        walletToReplace.setId(walletDB.getId());
        walletRepository.save(walletToReplace);
    }
}
