package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.model.*;
import com.lazyants.filecessor.security.UserAuthentication;
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
@RequestMapping(value = "/api", produces = {"application/json"})
public class PhotoController {

    private final PhotoRepository photoRepository;

    private final PhotoSaver photoSaver;

    private PhotoResourceAssembler photoResourceAssembler = new PhotoResourceAssembler(PhotoController.class, PhotoResource.class);

    @Autowired
    public PhotoController(PhotoRepository photoRepository, PhotoSaver photoSaver) {
        this.photoRepository = photoRepository;
        this.photoSaver = photoSaver;
    }

    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public PagedResources<PhotoResource> list(Pageable pageable, PagedResourcesAssembler<Photo> pagedAssembler) {
        return pagedAssembler.toResource(photoRepository.findAll(pageable), photoResourceAssembler);
    }

    @RequestMapping(value = "/me/photos", method = RequestMethod.GET)
    public PagedResources<PhotoResource> meList(Pageable pageable, UserAuthentication authentication,
                                                PagedResourcesAssembler<Photo> pagedAssembler) {
        return pagedAssembler.toResource(photoRepository.findByUserId(pageable, authentication.getDetails().getUsername()), photoResourceAssembler);
    }

    @RequestMapping(value = "/photos/{id}", method = RequestMethod.GET)
    public PhotoResource get(@PathVariable("id") String id) {
        return new PhotoResource(photoRepository.findOne(id));
    }

    @RequestMapping(value = "/photos/upload", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Photo upload(@Valid @ModelAttribute PhotoFile file, UserAuthentication authentication) {
        return photoSaver.saveFile(file, authentication);
    }

    @RequestMapping(value = "/photos/image-url", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Photo downloadImageByUrl(@RequestParam String url, UserAuthentication authentication) {
        return photoSaver.downloadImage(url, authentication);
    }

    @RequestMapping(value = "/photos/video", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    Photo downloadVideoThumb(@RequestParam(name = "video-url") String videoUrl, UserAuthentication authentication) {
        return photoSaver.downloadImage(MetadataDownloader.getThumbnailUrl(videoUrl), authentication);
    }
}
