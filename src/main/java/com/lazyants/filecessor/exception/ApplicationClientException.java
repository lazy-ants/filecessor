package com.lazyants.filecessor.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class ApplicationClientException extends HttpClientErrorException {
    public ApplicationClientException() {
        this(HttpStatus.BAD_REQUEST);
    }

    public ApplicationClientException(HttpStatus statusCode) {
        super(statusCode);
    }

    public ApplicationClientException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public ApplicationClientException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
        super(statusCode, statusText, responseBody, responseCharset);
    }

    public ApplicationClientException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
    }

    public ApplicationClientException(String statusText) {
        this(HttpStatus.BAD_REQUEST, statusText);
    }
}
