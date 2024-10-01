package com.projeto.picpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class SenderEqualsToReciverIdException extends PicPayException {
    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setDetail("You can't do a transfer that id of sender it's equal to id of reciver");

        return pb;
    }
}
