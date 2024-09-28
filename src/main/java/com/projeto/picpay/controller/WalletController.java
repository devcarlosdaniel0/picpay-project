package com.projeto.picpay.controller;

import com.projeto.picpay.domain.Wallet;
import com.projeto.picpay.requests.WalletPostRequestBody;
import com.projeto.picpay.requests.WalletPutRequestBody;
import com.projeto.picpay.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<List<Wallet>> findAll(){
        return ResponseEntity.ok(walletService.findAll());
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody @Valid WalletPostRequestBody walletPostRequestBody) {
        return new ResponseEntity<>(walletService.createWallet(walletPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        walletService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid WalletPutRequestBody walletPutRequestBody) {
        walletService.replace(walletPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
