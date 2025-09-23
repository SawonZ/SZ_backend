package com.atomz.sawonz.global.exception;

import lombok.*;

@Getter
@Setter
@Builder
public class ErrorException extends RuntimeException {

    private final ResponseCode responseCode;
    private String errorMessage;

    public ErrorException(ResponseCode responseCode) {
        super(responseCode.getDescription()); // ← 추가
        this.responseCode = responseCode;
        this.errorMessage = responseCode.getDescription();
    }

    public ErrorException(ResponseCode responseCode, String message) {
        super(message); // ← 추가
        this.responseCode = responseCode;
        this.errorMessage = message;
    }

}
