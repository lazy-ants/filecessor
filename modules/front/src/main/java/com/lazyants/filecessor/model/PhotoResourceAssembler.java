package com.lazyants.filecessor.model;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PhotoResourceAssembler extends ResourceAssemblerSupport<Photo, PhotoResource> {
    public PhotoResourceAssembler(Class<?> controllerClass, Class<PhotoResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public PhotoResource toResource(Photo entity) {
        return new PhotoResource(entity);
    }
}
