package com.lazyants.filecessor.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.handling.TransformationBuilder;
import com.lazyants.filecessor.utils.OperationCreator;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageHandler {

    private final ApplicationConfiguration configuration;

    @Autowired
    public ImageHandler(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    public BufferedImage transform(String transformation, File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return new TransformationBuilder(image)
                    .addOperations(OperationCreator.createOperations(transformation))
                    .applyTransformations();
        } catch (IOException ignore) {}

        return null;
    }

    public BufferedImage cropByCordinates(String filename, String ext, int x1, int y1, int x2, int y2) {
        try {
            File file = new File(configuration.getMediaDirectoryPath() + filename + "." + ext);
            BufferedImage image = ImageIO.read(file);
            Scalr.Rotation rotation = rotation(readRotationFromMetadata(file));

            if (rotation != null) {
                image = Scalr.rotate(image, rotation);
            }

            return Scalr.crop(image, x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
        } catch (IOException ignore) {}

        return null;
    }

    public BufferedImage rotate(String filename, String ext, int degrees) {
        try {
            File file = new File(configuration.getMediaDirectoryPath() + filename + "." + ext);
            BufferedImage image = ImageIO.read(file);
            Scalr.Rotation rotation = rotation(readRotationFromMetadata(file) + degrees);

            if (rotation != null) {
                image = Scalr.rotate(image, rotation);
            }

            return image;
        } catch (IOException ignore) {}

        return null;
    }

    private int readRotationFromMetadata(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (exifIFD0 == null) {
                return 0;
            }
            int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            switch (orientation) {
                case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                    return 90;
                case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                    return 180;
                case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                    return 270;
            }
        } catch (ImageProcessingException | IOException | MetadataException ignore) {}

        return 0;
    }

    private Scalr.Rotation rotation(int degrees) {
        if (degrees >= 360) {
            degrees -= 360;
        }

        switch (degrees) {
            case 90:
                return Scalr.Rotation.CW_90;
            case 180:
                return Scalr.Rotation.CW_180;
            case 270:
                return Scalr.Rotation.CW_270;
        }

        return null;
    }
}
