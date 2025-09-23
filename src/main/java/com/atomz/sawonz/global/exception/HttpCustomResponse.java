package com.atomz.sawonz.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpCustomResponse<T> {

    private int statusCode;
    private String message;
    private ResponseCode responseCode;
    private T data;

    public HttpCustomResponse(ResponseCode responseCode, String message) {
        this.statusCode = responseCode.getStatus().value();
        this.message = message;
        this.responseCode = responseCode;
    }

    public HttpCustomResponse(ResponseCode responseCode, T data) {
        this.statusCode = responseCode.getStatus().value();
        this.message = responseCode.getDescription();
        this.responseCode = responseCode;
        this.data = data;
    }

}
