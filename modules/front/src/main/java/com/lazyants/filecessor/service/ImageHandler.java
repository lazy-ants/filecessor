package com.lazyants.filecessor.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.lazyants.filecessor.handling.Rotate;
import com.lazyants.filecessor.handling.TransformationBuilder;
import com.lazyants.filecessor.utils.ImageReader;
import com.lazyants.filecessor.utils.OperationCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ImageHandler.class);

    public BufferedImage transform(String transformation, File file) {
        long time = System.currentTimeMillis();
        BufferedImage image = ImageReader.read(file);
        logger.info("Image reads: " + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        TransformationBuilder builder = new TransformationBuilder(image);

        int exifRotation = readRotationFromMetadata(file);
        logger.info("Orientation reads: " + (System.currentTimeMillis() - time) + "ms");
        if (exifRotation > 0) {
            builder.addOperation(new Rotate(exifRotation));
        }

        time = System.currentTimeMillis();
        BufferedImage result = builder
                .addOperations(OperationCreator.createOperations(transformation))
                .applyTransformations();
        logger.info("Transformations: " + (System.currentTimeMillis() - time) + "ms");
        return result;
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
