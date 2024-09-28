package com.projeto.picpay.client;

import com.projeto.picpay.client.requests.AuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authorizationClient", url = "https://util.devi.tools/api/v2/authorize")
public interface AuthorizationClient {

    @GetMapping
    ResponseEntity<AuthorizationResponse> isAuthorized();
}
