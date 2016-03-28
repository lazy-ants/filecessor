package com.lazyants.filecessor.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Data
public class LinkBody {
    @NotBlank
    @URL
    private String url;
}
