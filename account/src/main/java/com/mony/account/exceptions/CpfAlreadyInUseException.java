package com.mony.account.exceptions;

public class CpfAlreadyInUseException extends RuntimeException {
    public CpfAlreadyInUseException(String message) {
        super(message);
    }
}