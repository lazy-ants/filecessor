package com.lazyants.filecessor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix="application")
public class ApplicationConfiguration {
    private String exiftoolPath;

    private String mediaDirectoryPath;

    private String[] formats;

    public void setFormats(String string) {
        this.formats = string.split(",");
    }
}
