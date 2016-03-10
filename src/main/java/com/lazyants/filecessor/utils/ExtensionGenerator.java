package com.lazyants.filecessor.utils;

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
}
