package com.lazyants.filecessor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingAdvice {
    @ResponseBody
    @ExceptionHandler(ApplicationClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ClientError applicationExceptionHandle(ApplicationClientException ex) {
        return new ClientError(ex.getMessage());
    }
}
