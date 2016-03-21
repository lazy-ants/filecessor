package com.lazyants.filecessor.utils;

import org.springframework.http.MediaType;

public class ExtensionGenerator {
    public static String getExtension(String contentType) {
        switch (contentType) {
            case "image/gif":
                return "gif";
            case "image/png":
                return "png";
            case "image/jpeg":
                return "jpg";
        }

        return null;
    }

    public static MediaType getMediaType(String extension) {
        switch (extension) {
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
        }

        return null;
    }
}
