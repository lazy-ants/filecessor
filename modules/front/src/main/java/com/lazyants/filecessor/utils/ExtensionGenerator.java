package com.lazyants.filecessor.utils;

public class ExtensionGenerator {
    public static final String IMAGE_GIF = "gif";
    public static final String IMAGE_TIFF = "tif";
    public static final String IMAGE_JPEG = "jpg";
    public static final String IMAGE_PNG = "png";

    public static String getExtension(String contentType) {
        switch (contentType) {
            case "image/gif":
                return IMAGE_GIF;
            case "image/png":
                return IMAGE_PNG;
            case "image/jpeg":
                return IMAGE_JPEG;
            case "image/tiff":
                return IMAGE_TIFF;
        }

        return null;
    }
}
