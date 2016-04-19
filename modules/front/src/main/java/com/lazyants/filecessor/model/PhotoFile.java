package com.lazyants.filecessor.model;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
public class PhotoFile {

    @NotNull
    private String fileContentType;

    @NotNull
    private String fileName;

    @NotNull
    private long fileSize;

    @NotNull
    private String filePath;

    @AssertTrue
    public boolean isTypeValid() {
        return fileContentType.equals("image/jpeg") || fileContentType.equals("image/gif") || fileContentType.equals("image/png");
    }
}
