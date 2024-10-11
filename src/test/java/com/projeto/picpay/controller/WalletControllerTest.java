package com.projeto.picpay.controller;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.requests.WalletPostRequestBody;
import com.projeto.picpay.requests.WalletPutRequestBody;
import com.projeto.picpay.requests.WithdrawalOrDepositRequestDTO;
import com.projeto.picpay.requests.WithdrawalOrDepositResponse;
import com.projeto.picpay.service.WalletService;
import com.projeto.picpay.util.Wallet.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
class WalletControllerTest {
    @Mock
    private WalletService walletServiceMock;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    void setUp() {
        BDDMockito.when(walletServiceMock.findAll())
                .thenReturn(List.of(WalletCreator.createValidWallet()));

        BDDMockito.when(walletServiceMock.createWallet(ArgumentMatchers.any(WalletPostRequestBody.class)))
                .thenReturn(WalletCreator.createWalletToBeSaved());

        BDDMockito.when(walletServiceMock.deposit(ArgumentMatchers.any(WithdrawalOrDepositRequestDTO.class)))
                .thenReturn(DepositResponseCreator.createDepositResponse());

        BDDMockito.when(walletServiceMock.withdrawal(ArgumentMatchers.any(WithdrawalOrDepositRequestDTO.class)))
                .thenReturn(WithdrawalResponseCreator.createWithdrawalResponse());

        BDDMockito.doNothing().when(walletServiceMock).delete(ArgumentMatchers.anyLong());

        BDDMockito.doNothing().when(walletServiceMock).replace(ArgumentMatchers.any());
    }

    @Test
    @DisplayName(value = "findAll returns list of wallets when successful")
    void findAll_ReturnsListOfWallets_WhenSuccessful() {
        List<Wallet> wallets = walletController.findAll().getBody();

        String expectedName = WalletCreator.createValidWallet().getFullName();

        Assertions.assertThat(wallets)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(wallets.get(0).getFullName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName(value = "findAll return a empty list when there is no wallet")
    void findAll_ReturnEmptyList_WhenThereIsNoWallet() {
        BDDMockito.when(walletServiceMock.findAll())
                .thenReturn(List.of());

        List<Wallet> wallets = walletController.findAll().getBody();

        Assertions.assertThat(wallets)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName(value = "createWallet return a wallet when successful")
    void createWallet_ReturnWallet_WhenSuccessful() {
        Wallet wallet = walletController.createWallet(WalletPostRequestBodyCreator.createWalletPostRequestBody()).getBody();

        Assertions.assertThat(wallet).isNotNull().isEqualTo(WalletCreator.createWalletToBeSaved());
    }

    @Test
    @DisplayName(value = "deposit return WithdrawalOrDepositResponse when successful")
    void deposit_ReturnWithdrawalOrDepositResponse_WhenSuccessful() {
        WithdrawalOrDepositResponse depositResponse = walletController.deposit(RequestDTOCreator.createRequestDTO()).getBody();

        Assertions.assertThat(depositResponse).isNotNull();
        Assertions.assertThat(depositResponse.walletID()).isEqualTo(1);
        Assertions.assertThat(depositResponse.balance()).isEqualByComparingTo(BigDecimal.valueOf(15));
        Assertions.assertThat(depositResponse.value()).isEqualByComparingTo(BigDecimal.valueOf(5));
    }

    @Test
    @DisplayName(value = "withdrawal return WithdrawalOrDepositResponse when successful")
    void withdrawal_ReturnWithdrawalOrDepositResponse_WhenSuccessful() {
        WithdrawalOrDepositResponse depositResponse = walletController.withdrawal(RequestDTOCreator.createRequestDTO()).getBody();

        Assertions.assertThat(depositResponse).isNotNull();
        Assertions.assertThat(depositResponse.walletID()).isEqualTo(1);
        Assertions.assertThat(depositResponse.balance()).isEqualByComparingTo(BigDecimal.valueOf(5));
        Assertions.assertThat(depositResponse.value()).isEqualByComparingTo(BigDecimal.valueOf(5));
    }

    @Test
    @DisplayName(value = "delete removes wallet when successful")
    void delete_RemovesWallet_WhenSuccessful() {
        ResponseEntity<Void> entity = walletController.delete(1L);

        Assertions.assertThatCode(() -> walletController.delete(1L)).doesNotThrowAnyException();

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName(value = "replace updates wallet when sucessful")
    void replace_UpdatesWallet_WhenSucessful() {
        ResponseEntity<Void> entity = walletController.replace(WalletPutRequestBodyCreator.createWalletPutRequestBody());

        Assertions.assertThatCode(() -> walletController.replace(WalletPutRequestBodyCreator.createWalletPutRequestBody())).doesNotThrowAnyException();

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}