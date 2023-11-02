package com.cit.festival.exception;


import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {
    private String message;

    private String httpStatus = HttpStatus.BAD_REQUEST.toString() ;

    public PaymentException(String message) {
        this.message = message;
    }
}
