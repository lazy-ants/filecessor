package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.configuration.RabbitConfig;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.utils.ExtensionGenerator;
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

        File to = new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
        File from = new File(file.getFilePath());
        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error moving from " + from.getAbsolutePath() + " to " + to.getAbsolutePath());
            return new ResponseEntity<String>("Bad path", HttpStatus.BAD_REQUEST);
        }
        rabbitTemplate.convertAndSend(rabbitConfig.getQueueName(), photo.getId());

        return new ResponseEntity<Photo>(photo, HttpStatus.CREATED);
    }
}
