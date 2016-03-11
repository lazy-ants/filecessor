package com.lazyants.filecessor.utils;

import com.lazyants.filecessor.exception.ApplicationClientException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataDownloader {

    public static String getThumbnailUrl(String url) throws ApplicationClientException {
        RestTemplate rest = new RestTemplate();
        try {
            HashMap map = rest.getForObject(getMetadataUrl(url), HashMap.class);
            return (String) map.get("thumbnail_url");
        } catch (RestClientException ignored) {}

        throw new ApplicationClientException("Thumbnail not found");
    }

    public static String getMetadataUrl(String url) throws ApplicationClientException {
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

        throw new ApplicationClientException("Invalid video url");
    }
}
