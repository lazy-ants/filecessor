package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.service.ImageHandler;
import com.lazyants.filecessor.utils.ExtensionGenerator;
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

    private final ApplicationConfiguration configuration;
    private final ImageHandler handler;

    @Autowired
    public ResizeController(ApplicationConfiguration configuration, ImageHandler handler) {
        this.configuration = configuration;
        this.handler = handler;
    }

    @RequestMapping("/transform/{filters}/{filename}.{ext}")
    public ResponseEntity<byte[]> transform(@PathVariable String filename, @PathVariable String ext, @PathVariable("filters") String filters) {
        File file = new File(configuration.getMediaDirectoryPath() + filename + "." + ext);
        return renderImage(handler.transform(filters, file), ext);
    }

    private ResponseEntity<byte[]> renderImage(BufferedImage image, String ext) {

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(ExtensionGenerator.getMediaType(ext));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, ext, stream);
            stream.flush();
            byte[] result = stream.toByteArray();
            stream.close();
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } catch (IOException ignore) {}

        return null;
    }
}
