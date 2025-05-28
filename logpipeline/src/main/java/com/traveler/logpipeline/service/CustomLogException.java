package com.traveler.logpipeline.service;

public class CustomLogException extends RuntimeException{
    public CustomLogException(String message) {
        super(message);
    }
}