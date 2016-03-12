package com.lazyants.filecessor.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
class PhotoResourceProcessor implements ResourceProcessor<Resource<Photo>> {

    @Override
    public Resource<Photo> process(Resource<Photo> photoResource) {
        String[] formats = new String[] {"medium", "thumb", "small", "big", "original", "mobile"};
        Photo photo = photoResource.getContent();

        for (String format: formats) {
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
