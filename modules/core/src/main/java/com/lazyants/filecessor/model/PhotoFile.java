package com.lazyants.filecessor.model;

import lombok.Data;

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
}
