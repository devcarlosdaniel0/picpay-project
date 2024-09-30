package com.projeto.picpay.requests;

import java.math.BigDecimal;

public interface TransferRequestBody {
    BigDecimal value();
    Long senderID();
    Long reciverID();
}
