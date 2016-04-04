package com.lazyants.filecessor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Exif {

    @Field("datetime_original")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date datetimeOriginal;

    @Field("exposure_time")
    private String exposureTime;

    private String aperture;

    @Field("focal_length")
    private String focalLength;

    @Field("color_space")
    private String colorSpace;

    @Field("pixel_x_dimension")
    private double pixelXDimension;

    @Field("pixel_y_dimension")
    private double pixelYDimension;

    private int width;

    private int height;

    private String iso;

    private double latitude;

    private double longitude;

    private String camera;

    private String lens;

}
