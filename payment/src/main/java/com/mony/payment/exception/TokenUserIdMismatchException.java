package com.mony.payment.exception;

public class TokenUserIdMismatchException extends RuntimeException {
    public TokenUserIdMismatchException(String message) {
        super(message);
    }
}
