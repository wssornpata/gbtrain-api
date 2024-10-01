package com.exercise.gbtrain.exception;

import lombok.Getter;

@Getter
public class InvalidEntityAndTypoException extends RuntimeException {
    private final String detailMessage;

    public InvalidEntityAndTypoException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }
}