package com.lazyants.filecessor.handling;

import com.lazyants.filecessor.exception.ApplicationClientException;
import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class CropCoordinates extends Operation {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    public CropCoordinates(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        if (x1 > image.getWidth() || x2 > image.getWidth() || y1 > image.getHeight() || y2 > image.getHeight()
                || x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) {
            throw new ApplicationClientException("Coordinates is out of bounds");
        }
        return Scalr.crop(image, x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}
