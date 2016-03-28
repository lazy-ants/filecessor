package com.lazyants.filecessor.service;

import com.lazyants.filecessor.configuration.BaseApplicationConfiguration;
import com.lazyants.filecessor.exception.ApplicationClientException;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.security.UserAuthentication;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Component
public class PhotoSaver {
    private final PhotoRepository photoRepository;

    private final BaseApplicationConfiguration configuration;

    private final RabbitPublisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(PhotoSaver.class);

    @Autowired
    public PhotoSaver(PhotoRepository photoRepository, BaseApplicationConfiguration configuration, RabbitPublisher publisher) {
        this.photoRepository = photoRepository;
        this.configuration = configuration;
        this.publisher = publisher;
    }

    public Photo downloadImage(String url, UserAuthentication authentication) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            String extension = ExtensionGenerator.getExtension(uc.getContentType());
            if (extension == null) {
                throw new IOException("Invalid type");
            }
            Photo photo = new Photo();
            photo.setExtension(extension);
            if (authentication != null) {
                photo.setUserId(authentication.getDetails().getUsername());
            }
            photoRepository.save(photo);

            BufferedImage image = ImageIO.read(uc.getInputStream());
            File out = new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
            out.setReadable(true);
            ImageIO.write(image, "jpg", out);
            publisher.publishPhotoId(photo.getId());

            return photo;
        } catch (IOException ignored) {}

        throw new ApplicationClientException("Unable to download image");
    }

    public Photo saveFile(PhotoFile file, UserAuthentication authentication) {
        Photo photo = new Photo();
        photo.setExtension(ExtensionGenerator.getExtension(file.getFileContentType()));
        photo.setContentSize(file.getFileSize());
        photo.setFilename(file.getFileName());
        if (authentication != null) {
            photo.setUserId(authentication.getDetails().getUsername());
        }
        photoRepository.save(photo);

        File to = new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension());
        File from = new File(file.getFilePath());
        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error moving from " + from.getAbsolutePath() + " to " + to.getAbsolutePath());
            throw new ApplicationClientException("Bad path");
        }
        publisher.publishPhotoId(photo.getId());

        return photo;
    }
}
