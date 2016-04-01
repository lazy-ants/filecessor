package com.lazyants.filecessor.handling;

import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class Crop extends Operation {
    private final int width;
    private final int height;

    public Crop(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return Scalr.crop(image, width, height);
    }
}
