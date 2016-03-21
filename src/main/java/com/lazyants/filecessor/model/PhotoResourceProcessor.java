package com.lazyants.filecessor.model;

import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.UriTemplate;
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

            String path = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/")
                    .build()
                    .toString();

            photoResource.add(new Link(new UriTemplate(path + "resize_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "resize"));
            photoResource.add(new Link(new UriTemplate(path + "crop_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "crop"));
            photoResource.add(new Link(path + "original/" + photo.getId() + "." + photo.getExtension(), "original"));

        return photoResource;
    }
}
