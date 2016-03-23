package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/photos")
public class PhotoController {

    private final PhotoRepository repository;

    @Autowired
    public PhotoController(PhotoRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Page<Photo> readPhotos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
