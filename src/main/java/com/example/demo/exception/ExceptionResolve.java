package com.example.demo.exception;

import com.example.demo.exception.entity.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionResolve {

    @ExceptionHandler({ResponseStatusException.class})
    public ExceptionResponse responseStatusException(ResponseStatusException responseStatusException, HttpServletResponse httpServletResponse) {


        log.error("ERROR 발생 내용 : " + responseStatusException.getReason());
        ExceptionResponse exceptionResponse = new ExceptionResponse(responseStatusException);
        httpServletResponse.setStatus(responseStatusException.getRawStatusCode());
        return exceptionResponse;
    }


}
