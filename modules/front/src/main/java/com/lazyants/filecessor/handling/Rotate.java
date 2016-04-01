package com.lazyants.filecessor.handling;

import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class Rotate extends Operation {

    private Scalr.Rotation degrees;

    public Rotate(int degrees) {
        if (degrees >= 360) {
            degrees -= 360;
        }

        switch (degrees) {
            case 90:
                this.degrees = Scalr.Rotation.CW_90;
                break;
            case 180:
                this.degrees = Scalr.Rotation.CW_180;
                break;
            case 270:
                this.degrees = Scalr.Rotation.CW_270;
                break;
        }
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return Scalr.rotate(image, degrees);
    }
}
