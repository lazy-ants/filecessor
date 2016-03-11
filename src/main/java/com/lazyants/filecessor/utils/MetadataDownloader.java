package com.lazyants.filecessor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataDownloader {

    public static String getMetadataUrl(String url) {
        if (url.contains("youtu")) {
            Pattern pattern = Pattern.compile("(?<=v(=|/))([-a-zA-Z0-9_]+)|(?<=youtu.be/)([-a-zA-Z0-9_]+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return String.format("http://www.youtube.com/oembed?url=http://www.youtube.com/watch?v=%s&format=json", matcher.group());
            }
        }
        if (url.contains("vimeo")) {
            String pattern = "vimeo\\.com/(\\d+)";
            Pattern r = Pattern.compile(pattern);
            Matcher matcher = r.matcher(url);
            if (matcher.find()) {
                return String.format("http://vimeo.com/api/oembed.json?url=http://vimeo.com/%s", matcher.group(1));
            }
        }
        return null;
    }
}
