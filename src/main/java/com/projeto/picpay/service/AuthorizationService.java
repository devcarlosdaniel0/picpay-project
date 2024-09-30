package com.projeto.picpay.service;

import com.projeto.picpay.client.AuthorizationClient;
import com.projeto.picpay.exception.PicPayException;
import com.projeto.picpay.requests.TransferRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final AuthorizationClient authorizationClient;

    public boolean isAuthorized(TransferRequestBody transfer) {
        var response = authorizationClient.isAuthorized();

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new PicPayException();
        }

        return response.getBody().data().authorization();
    }
}
