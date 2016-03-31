package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RestController
public class ResizeController {
    ApplicationConfiguration configuration;

    @Autowired
    public ResizeController(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @RequestMapping("/crop_coordinates_{x1}x{y1}_{x2}x{y2}/{filename}.{ext}")
    public ResponseEntity<byte[]> coordinatesResize(@PathVariable int x1, @PathVariable int x2, @PathVariable int y1,
                                                    @PathVariable int y2, @PathVariable String filename, @PathVariable String ext) {
        try {
            BufferedImage image = ImageIO.read(new File(configuration.getMediaDirectoryPath() + filename + "." + ext));
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(ExtensionGenerator.getMediaType(ext));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(Scalr.crop(image, x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2)), ext, stream);
            stream.flush();
            byte[] result = stream.toByteArray();
            stream.close();

            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } catch (IOException ignore) {}

        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Image not found");
    }
}
