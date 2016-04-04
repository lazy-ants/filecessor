package com.lazyants.filecessor.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetadataTest {
    @Test
    public void test_metadata() {

        assertEquals(MetadataDownloader.getMetadataUrl("https://vimeo.com/158065984"), "http://vimeo.com/api/oembed.json?url=http://vimeo.com/158065984");

        assertEquals(MetadataDownloader.getMetadataUrl("https://www.youtube.com/watch?v=HjLk0wi5WrA"), "http://www.youtube.com/oembed?url=http://www.youtube.com/watch?v=HjLk0wi5WrA&format=json");

        assertEquals(MetadataDownloader.getMetadataUrl("youtu.be/o8nm1N-4Tjk"), "http://www.youtube.com/oembed?url=http://www.youtube.com/watch?v=o8nm1N-4Tjk&format=json");
    }
}
