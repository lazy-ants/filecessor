package com.lazyants.filecessor.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.lazyants.filecessor.handling.Rotate;
import com.lazyants.filecessor.handling.TransformationBuilder;
import com.lazyants.filecessor.utils.OperationCreator;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageHandler {

    public BufferedImage transform(String transformation, File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            TransformationBuilder builder = new TransformationBuilder(image);

            int exifRotation = readRotationFromMetadata(file);
            if (exifRotation > 0) {
                builder.addOperation(new Rotate(exifRotation));
            }

            return builder
                    .addOperations(OperationCreator.createOperations(transformation))
                    .applyTransformations();
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
}
