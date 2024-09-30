package com.projeto.picpay.controller;

import com.projeto.picpay.domain.Transfer;
import com.projeto.picpay.requests.TransferPostRequestBody;
import com.projeto.picpay.requests.TransferPutRequestBody;
import com.projeto.picpay.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("transfers")
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody @Valid TransferPostRequestBody transferPostRequestBody) {
        return new ResponseEntity<>(transferService.transfer(transferPostRequestBody), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> findAll() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transferService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid TransferPutRequestBody transferPutRequestBody) {
        transferService.replace(transferPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
