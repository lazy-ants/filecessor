package com.lazyants.filecessor.handling;

import com.lazyants.filecessor.exception.ApplicationClientException;
import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class Rotate extends Operation {

    private int degrees;

    public Rotate(int degrees) {
        if (degrees >= 360) {
            degrees -= 360;
        }
        this.degrees = degrees;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        switch (degrees) {
            case 90:
                return Scalr.rotate(image, Scalr.Rotation.CW_90);
            case 180:
                return Scalr.rotate(image, Scalr.Rotation.CW_180);
            case 270:
                return Scalr.rotate(image, Scalr.Rotation.CW_270);
        }

        throw new ApplicationClientException("Invalid rotation argument");
    }
}
