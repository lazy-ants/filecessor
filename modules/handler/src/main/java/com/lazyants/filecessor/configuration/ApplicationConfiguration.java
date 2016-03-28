package com.lazyants.filecessor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="application")
public class ApplicationConfiguration extends BaseApplicationConfiguration {
    private String exiftoolPath;
}
