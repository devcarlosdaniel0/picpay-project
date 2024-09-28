package com.projeto.picpay.client;

import com.projeto.picpay.domain.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "https://util.devi.tools/api/v1/notify")
public interface NotificationClient {

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody Transfer transfer);
}
