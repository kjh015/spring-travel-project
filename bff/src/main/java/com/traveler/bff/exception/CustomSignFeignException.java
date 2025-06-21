package com.traveler.bff.exception;

public class CustomSignFeignException extends RuntimeException {
    public CustomSignFeignException(String message) {
        super(message);
    }
}
