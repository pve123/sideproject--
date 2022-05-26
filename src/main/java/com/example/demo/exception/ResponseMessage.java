package com.example.demo.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {

    private Integer statusCode;
    private String Message;
}
