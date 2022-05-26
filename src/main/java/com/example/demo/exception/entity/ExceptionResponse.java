package com.example.demo.exception.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private Integer errorCode;
    private String errorMessage;


    public ExceptionResponse(ResponseStatusException responseStatusException) {
        this.timestamp = LocalDateTime.now();
        this.errorCode = responseStatusException.getRawStatusCode();
        this.errorMessage = responseStatusException.getReason();
    }

    ;

}
