package com.atomz.sawonz.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<HttpCustomResponse> handleErrorException(ErrorException e) {
        HttpCustomResponse errorResponse = new HttpCustomResponse(e.getResponseCode(), e.getErrorMessage());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

}
