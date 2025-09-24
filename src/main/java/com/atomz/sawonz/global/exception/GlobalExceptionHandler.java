package com.atomz.sawonz.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<HttpCustomResponse> handleErrorException(ErrorException e) {
        HttpCustomResponse errorResponse = new HttpCustomResponse(e.getResponseCode(), e.getErrorMessage());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpCustomResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream()
                .findFirst().map(err -> err.getDefaultMessage()).orElse("유효성 검사 실패");
        HttpCustomResponse<Void> body = new HttpCustomResponse<>(ResponseCode.BAD_REQUEST, msg);
        return new ResponseEntity<>(body, ResponseCode.BAD_REQUEST.getStatus());
    }


}
