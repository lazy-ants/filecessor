package com.lazyants.filecessor.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
class PhotoResourceProcessor implements ResourceProcessor<Resource<Photo>> {

    Logger logger = LoggerFactory.getLogger(PhotoResourceProcessor.class);

    @Override
    public Resource<Photo> process(Resource<Photo> photoResource) {
        String[] formats = new String[] {"medium", "thumb", "small", "big", "original", "mobile"};

        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            photoResource.getContent().getExtension();
            for (String format: formats) {
                photoResource.add(new Link((host + "/media/" + format + "/" + photoResource.getContent().getId() + "."
                        + photoResource.getContent().getExtension())));
            }
        } catch (UnknownHostException e) {
            logger.error("Unknown host");
        }

        return photoResource;
    }
}
