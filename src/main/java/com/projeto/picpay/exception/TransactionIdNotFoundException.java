package com.projeto.picpay.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@RequiredArgsConstructor
public class TransactionIdNotFoundException extends PicPayException {
    private final String detail;

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Transaction ID not found");
        pb.setDetail(detail);

        return pb;
    }
}
