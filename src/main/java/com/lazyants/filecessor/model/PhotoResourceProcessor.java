package com.lazyants.filecessor.model;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
class PhotoResourceProcessor implements ResourceProcessor<Resource<Photo>> {

    private ApplicationConfiguration configuration;

    @Autowired
    public PhotoResourceProcessor(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Resource<Photo> process(Resource<Photo> photoResource) {

        Photo photo = photoResource.getContent();

        for (String format: configuration.getFormats()) {
            String path = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/media/{format}/{id}.{ext}")
                    .buildAndExpand(format, photo.getId(), photo.getExtension())
                    .toString();
            photoResource.add(new Link(path, format));
        }

        return photoResource;
    }
}
