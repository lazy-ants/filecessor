package com.lazyants.filecessor.worker;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.service.ColorFinder;
import com.lazyants.filecessor.service.ExifParser;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

@Component
public class Receiver {

    public static final int REQUIRED_WIDTH = 1500;

    private ExifParser parser;

    private ColorFinder finder;

    private PhotoRepository repository;

    private ApplicationConfiguration configuration;

    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    public Receiver(ColorFinder finder, PhotoRepository repository, ApplicationConfiguration configuration) {
        this.parser = new ExifParser(configuration.getExiftoolPath());
        this.finder = finder;
        this.repository = repository;
        this.configuration = configuration;
    }

    public void receiveMessage(String message) {
        long time = System.currentTimeMillis();
        Photo photo = repository.findOne(message);
        try {
            if (photo != null) {
                File photoFile = new File(configuration.getOriginalDirectory() + photo.getId() + "." + photo.getExtension());
                photo.setExif(parser.parseExif(photoFile));

                BufferedImage original = ImageIO.read(photoFile);
                BufferedImage scaledImg = Scalr.resize(original, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, REQUIRED_WIDTH, original.getHeight() * REQUIRED_WIDTH / original.getWidth());
                photo.setColors(Arrays.asList(finder.findColors(scaledImg)));

                File regularFile = new File(configuration.getRegularDirectory() + photo.getId() + "." + photo.getExtension());
                regularFile.setReadable(true);
                ImageIO.write(scaledImg, photo.getExtension(), regularFile);

                logger.info("Execution of image " + photo.getId() + " processing: " + (System.currentTimeMillis() - time) + "ms");
            }
        } catch (Exception e) {
            logger.error("Error with photo " + photo.getId() + ": " + e.getMessage());
        } finally {
            repository.save(photo);
        }
    }
}
