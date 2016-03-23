package com.lazyants.filecessor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application")
public class ApplicationConfiguration extends BaseApplicationConfiguration {
}
