package com.projeto.picpay.controller;

import com.projeto.picpay.domain.Transfer;
import com.projeto.picpay.requests.TransferPostRequestBody;
import com.projeto.picpay.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<Transfer> transfer(@RequestBody @Valid TransferPostRequestBody transferPostRequestBody) {
        var response = transferService.transfer(transferPostRequestBody);

        return ResponseEntity.ok(response);
    }
}
