package com.projeto.picpay.service;

import com.projeto.picpay.client.NotificationClient;
import com.projeto.picpay.domain.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
    private NotificationClient notificationClient;

    public void sendNotification(Transfer transfer) {

        try {
            log.info("Sending notification...");
            var response = notificationClient.sendNotification(transfer);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                log.error("Error while sending notification, status code is not OK");
            }

        } catch (Exception e) {
            log.error("Error while sending notification", e);
        }

    }
}
