package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.configuration.BaseApplicationConfiguration;
import com.lazyants.filecessor.configuration.BaseRabbitConfig;
import com.lazyants.filecessor.exception.ClientError;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.service.ImageDownloader;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import com.lazyants.filecessor.utils.MetadataDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
public class UploadController {

    private final BaseRabbitConfig rabbitConfig;

    private final BaseApplicationConfiguration configuration;

    private final PhotoRepository photoRepository;

    private final RabbitTemplate rabbitTemplate;

    private final ImageDownloader imageDownloader;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    public UploadController(PhotoRepository photoRepository, RabbitTemplate rabbitTemplate,
                            BaseApplicationConfiguration configuration, BaseRabbitConfig rabbitConfig, ImageDownloader imageDownloader) {
        this.photoRepository = photoRepository;
        this.configuration = configuration;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitConfig = rabbitConfig;
        this.imageDownloader = imageDownloader;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> upload(@Valid @ModelAttribute PhotoFile file) {
        Photo photo = new Photo();
        photo.setExtension(ExtensionGenerator.getExtension(file.getFileContentType()));
        photo.setContentSize(file.getFileSize());
        photo.setFilename(file.getFileName());
        photoRepository.save(photo);

        File to = new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
        File from = new File(file.getFilePath());
        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error moving from " + from.getAbsolutePath() + " to " + to.getAbsolutePath());
            return new ResponseEntity<>(new ClientError("Bad path"), HttpStatus.BAD_REQUEST);
        }
        rabbitTemplate.convertAndSend(rabbitConfig.getQueueName(), photo.getId());

        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/image-url", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Photo downloadImageByUrl(@RequestParam String url) {
        return imageDownloader.downloadImage(url);
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    Photo downloadVideoThumb(@RequestParam(name = "video-url") String videoUrl) {
        return imageDownloader.downloadImage(MetadataDownloader.getThumbnailUrl(videoUrl));
    }
}
