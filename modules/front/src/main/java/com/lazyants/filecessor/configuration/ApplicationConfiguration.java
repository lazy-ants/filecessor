package com.lazyants.filecessor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application")
@Data
public class ApplicationConfiguration extends BaseApplicationConfiguration {
    private String jwtSecret;
}
