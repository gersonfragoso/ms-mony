package com.mony.account.exceptions;


public class OtpExpiredOrInvalidException extends RuntimeException {
    public OtpExpiredOrInvalidException(String message) {
        super(message);
    }
}