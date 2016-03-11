package com.lazyants.filecessor.service;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.exception.ApplicationClientException;
import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Component
public class ImageDownloader {

    private PhotoRepository photoRepository;

    private ApplicationConfiguration configuration;

    @Autowired
    public ImageDownloader(PhotoRepository photoRepository, ApplicationConfiguration configuration) {
        this.photoRepository = photoRepository;
        this.configuration = configuration;
    }

    public Photo downloadImage(String url) {
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
            ImageIO.write(image, "jpg", new File(configuration.getMediaDirectoryPath() + photo.getId() + "." + photo.getExtension()));

            return photo;
        } catch (IOException ignored) {}

        throw new ApplicationClientException("Unable to download image");
    }
}
