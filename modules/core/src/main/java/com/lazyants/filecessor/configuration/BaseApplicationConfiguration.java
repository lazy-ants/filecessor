package com.lazyants.filecessor.configuration;

import lombok.Data;

@Data
public class BaseApplicationConfiguration {

    public static final String ORIGINAL_SUBDIRECTORY = "original";
    public static final String REGULAR_SUBDIRECTORY = "regular";

    private String mediaDirectoryPath;

    public String getOriginalDirectory() {
        return mediaDirectoryPath + "/" + ORIGINAL_SUBDIRECTORY + "/";
    }

    public String getRegularDirectory() {
        return mediaDirectoryPath + "/" + REGULAR_SUBDIRECTORY + "/";
    }
}
