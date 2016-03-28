package com.lazyants.filecessor.exception;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClientError {
    @NonNull
    private String message;
}
