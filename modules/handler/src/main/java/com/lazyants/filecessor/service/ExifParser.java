package com.lazyants.filecessor.service;

import com.lazyants.filecessor.utils.LatitudeLongitudeParser;
import com.lazyants.filecessor.model.Exif;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardFormat;
import com.thebuzzmedia.exiftool.core.StandardTag;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class ExifParser {
    private ExifTool tool;

    public ExifParser(String exifToolPath) {
        tool = new ExifToolBuilder()
                .withPath(exifToolPath)
                .build();
    }

    public Exif parseExif(File image) {
        try {
            Map<Tag, String> metadata = tool.getImageMeta(image, StandardFormat.HUMAN_READABLE, asList(
                    StandardTag.APERTURE,
                    StandardTag.MODEL,
                    StandardTag.LENS_ID,
                    StandardTag.COLOR_SPACE,
                    StandardTag.DATE_TIME_ORIGINAL,
                    StandardTag.EXPOSURE_TIME,
                    StandardTag.FOCAL_LENGTH,
                    StandardTag.ISO,
                    StandardTag.IMAGE_HEIGHT,
                    StandardTag.IMAGE_WIDTH,
                    StandardTag.GPS_LATITUDE,
                    StandardTag.GPS_LONGITUDE,
                    StandardTag.X_RESOLUTION,
                    StandardTag.Y_RESOLUTION
            ));

            return processMetadata(metadata);
        } catch (IOException e) {
            return null;
        }
    }

    private Exif processMetadata(Map<Tag, String> metadata) {
        Exif result = new Exif();
        result.setAperture(metadata.get(StandardTag.APERTURE));
        result.setCamera(metadata.get(StandardTag.MODEL));
        result.setLens(metadata.get(StandardTag.LENS_ID));
        result.setColorSpace(metadata.get(StandardTag.COLOR_SPACE));
        result.setExposureTime(metadata.get(StandardTag.EXPOSURE_TIME));
        result.setFocalLength(metadata.get(StandardTag.FOCAL_LENGTH));
        result.setIso(metadata.get(StandardTag.ISO));

        parseAndSetDate(metadata.get(StandardTag.DATE_TIME_ORIGINAL), result::setDatetimeOriginal);
        parseAndSetInt(metadata.get(StandardTag.IMAGE_HEIGHT), result::setHeight);
        parseAndSetInt(metadata.get(StandardTag.IMAGE_WIDTH), result::setWidth);
        parseAndSetLocation(metadata.get(StandardTag.GPS_LATITUDE), result::setLatitude);
        parseAndSetLocation(metadata.get(StandardTag.GPS_LONGITUDE), result::setLongitude);
        parseAndSetDouble(metadata.get(StandardTag.X_RESOLUTION), result::setPixelXDimension);
        parseAndSetDouble(metadata.get(StandardTag.Y_RESOLUTION), result::setPixelYDimension);

        return result;
    }

    private void parseAndSetInt(String value, Consumer<Integer> setter) {
        if (value != null) {
            try {
                setter.accept(Integer.valueOf(value));
            } catch (NumberFormatException e) { /*do nothing*/ }
        }
    }

    private void parseAndSetDouble(String value, Consumer<Double> setter) {
        if (value != null) {
            try {
                setter.accept(Double.valueOf(value));
            } catch (NumberFormatException e) { /*do nothing*/ }
        }
    }

    private void parseAndSetDate(String value, Consumer<Date> setter) {
        if (value != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy:M:dd hh:mm:ss");
                setter.accept(format.parse(value));
            } catch (ParseException e) { /* do nothing */ }
        }
    }

    private void parseAndSetLocation(String value, Consumer<Double> setter) {
        if (value != null) {
            double result = LatitudeLongitudeParser.ParseLatLonValue(value);
            if (result != Double.NaN) {
                setter.accept(result);
            }
        }
    }
}
