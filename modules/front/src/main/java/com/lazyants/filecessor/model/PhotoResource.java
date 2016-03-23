package com.lazyants.filecessor.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class PhotoResource extends ResourceSupport {
    private final Photo photo;

    public PhotoResource(Photo photo) {
        this.photo = photo;

        String path = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/")
                .build()
                .toString();

        add(new Link(new UriTemplate(path + "resize_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "resize"));
        add(new Link(new UriTemplate(path + "crop_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "crop"));
        add(new Link(path + "original/" + photo.getId() + "." + photo.getExtension(), "original"));
    }

    public Photo getPhoto() {
        return photo;
    }
}