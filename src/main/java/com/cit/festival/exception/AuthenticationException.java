package com.cit.festival.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private String message;

    private String httpStatus = HttpStatus.BAD_REQUEST.toString() ;

    public AuthenticationException(String message) {
        this.message = message;
    }
}
