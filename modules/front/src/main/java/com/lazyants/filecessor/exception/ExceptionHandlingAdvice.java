package com.lazyants.filecessor.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@ControllerAdvice
public class ExceptionHandlingAdvice {
    @ExceptionHandler(ApplicationClientException.class)
    ResponseEntity<String> applicationExceptionHandle(ApplicationClientException ex) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        Writer writer = new StringWriter();
        mapper.writeValue(writer, new ClientError(ex.getMessage()));

        return new ResponseEntity<>(writer.toString(), headers, ex.getStatusCode());
    }
}
