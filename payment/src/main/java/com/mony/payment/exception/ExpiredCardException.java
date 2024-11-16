package com.mony.payment.exception;

public class ExpiredCardException extends RuntimeException {
    public ExpiredCardException(String message) {
        super(message);
    }
}
