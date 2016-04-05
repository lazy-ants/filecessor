package com.lazyants.filecessor.utils;

import com.lazyants.filecessor.exception.ApplicationClientException;
import org.libjpegturbo.turbojpeg.TJDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class ImageReader {

    private static final Logger logger = LoggerFactory.getLogger(ImageReader.class);

    public static BufferedImage read(File file) {
        try {
            if (file.getName().endsWith(".jpg")) {
                long time = System.currentTimeMillis();
                FileInputStream inputStream = new FileInputStream(file);
                int size = inputStream.available();
                if(size < 1) {
                    throw new Exception();
                }

                byte[] bytes = new byte[size];
                inputStream.read(bytes);
                inputStream.close();
                logger.info("file reads:" + (System.currentTimeMillis() - time) + "ms");
                time = System.currentTimeMillis();
                TJDecompressor decompressor = new TJDecompressor(bytes);
                int width = decompressor.getWidth();
                int height = decompressor.getHeight();
                BufferedImage bufferedImage = decompressor.decompress(width, height, 1, 4096);
                logger.info("file decompress:" + (System.currentTimeMillis() - time) + "ms");

                return bufferedImage;
            }
            return ImageIO.read(file);
        } catch (Exception ignore) {}

        throw new ApplicationClientException(HttpStatus.NOT_FOUND, "Image not found");
    }
}
