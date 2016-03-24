package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.model.*;
import com.lazyants.filecessor.service.PhotoSaver;
import com.lazyants.filecessor.utils.MetadataDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/photos", produces = {"application/json"})
public class PhotoController {

    private final PhotoRepository photoRepository;

    private final PhotoSaver photoSaver;

    private PhotoResourceAssembler photoResourceAssembler = new PhotoResourceAssembler(PhotoController.class, PhotoResource.class);

    @Autowired
    public PhotoController(PhotoRepository photoRepository, PhotoSaver photoSaver) {
        this.photoRepository = photoRepository;
        this.photoSaver = photoSaver;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<PhotoResource> list(Pageable pageable, PagedResourcesAssembler<Photo> pagedAssembler) {
        return pagedAssembler.toResource(photoRepository.findAll(pageable), photoResourceAssembler);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PhotoResource get(@PathVariable("id") String id) {
        return new PhotoResource(photoRepository.findOne(id));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Photo upload(@Valid @ModelAttribute PhotoFile file) {
        return photoSaver.saveFile(file);
    }

    @RequestMapping(value = "/image-url", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Photo downloadImageByUrl(@RequestParam String url) {
        return photoSaver.downloadImage(url);
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    Photo downloadVideoThumb(@RequestParam(name = "video-url") String videoUrl) {
        return photoSaver.downloadImage(MetadataDownloader.getThumbnailUrl(videoUrl));
    }
}
