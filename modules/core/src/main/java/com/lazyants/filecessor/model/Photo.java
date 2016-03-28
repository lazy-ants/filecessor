package com.lazyants.filecessor.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "photo")
public class Photo {

    @Id
    private String id;

    private List<String> colors;

    @CreatedDate
    @Field("created_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    private String extension;

    @Field("user_id")
    private String userId;

    @Field("content_size")
    private long contentSize;

    private String filename;

    private Exif exif;

    @JsonIgnore
    private String fixtures;
}

