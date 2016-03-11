package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.configuration.RabbitConfig;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import com.lazyants.filecessor.utils.MetadataDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
public class UploadController {

    private final RabbitConfig rabbitConfig;

    private final ApplicationConfiguration configuration;

    private final PhotoRepository photoRepository;

    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    public UploadController(PhotoRepository photoRepository, RabbitTemplate rabbitTemplate,
                            ApplicationConfiguration configuration, RabbitConfig rabbitConfig) {
        this.photoRepository = photoRepository;
        this.configuration = configuration;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitConfig = rabbitConfig;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    ResponseEntity<?> upload(@Valid @ModelAttribute PhotoFile file) {
        Photo photo = new Photo();
        photo.setExtension(ExtensionGenerator.getExtension(file.getFileContentType()));
        photo.setContentSize(file.getFileSize());
        photo.setFilename(file.getFileName());
        photoRepository.save(photo);

        File to = getImageDestination(photo);
        File from = new File(file.getFilePath());
        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error moving from " + from.getAbsolutePath() + " to " + to.getAbsolutePath());
            return new ResponseEntity<>("Bad path", HttpStatus.BAD_REQUEST);
        }
        rabbitTemplate.convertAndSend(rabbitConfig.getQueueName(), photo.getId());

        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/image-url", method = RequestMethod.POST)
    ResponseEntity<?> downloadImageByUrl(@RequestParam String url) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            String extension = ExtensionGenerator.getExtension(uc.getContentType());
            if (extension == null) {
                throw new IOException("Invalid type");
            }
            Photo photo = new Photo();
            photo.setExtension(extension);
            photoRepository.save(photo);

            BufferedImage image = ImageIO.read(uc.getInputStream());
            ImageIO.write(image, "jpg", getImageDestination(photo));

            return new ResponseEntity<>(photo, HttpStatus.CREATED);
        } catch (IOException e) { /* nop */ }

        logger.error("Image url '" + url + "' is invalid");
        return new ResponseEntity<>("Invalid url", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    ResponseEntity<?> downloadVideoThumb(@RequestParam(name = "video-url") String videoUrl) {

        return new ResponseEntity<>(MetadataDownloader.getMetadataUrl(videoUrl), HttpStatus.BAD_REQUEST);
    }

    private File getImageDestination(Photo photo) {
        return new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
    }
}
