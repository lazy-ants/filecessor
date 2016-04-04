package com.lazyants.filecessor.handling;

import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class Resize extends Operation {
    private final int width;
    private final int height;

    public Resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return Scalr.resize(image, Math.min(width, image.getWidth()), Math.min(height, image.getHeight()));
    }
}
