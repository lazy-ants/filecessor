package com.lazyants.filecessor.worker;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.service.ColorFinder;
import com.lazyants.filecessor.service.ExifParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Component
public class Receiver {

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
        if (photo != null) {
            File photoFile = new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
            photo.setExif(parser.parseExif(photoFile));
            photo.setColors(Arrays.asList(finder.findColors(photoFile)));
            repository.save(photo);
            logger.info("Execution of image " + photo.getId() + " processing: " + (System.currentTimeMillis() - time) + "ms");
        }
    }
}
