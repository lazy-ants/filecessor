package com.lazyants.filecessor.controller;

import com.lazyants.filecessor.model.Photo;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import com.lazyants.filecessor.security.UserAuthentication;
import com.lazyants.filecessor.service.PhotoSaver;
import com.lazyants.filecessor.utils.MetadataDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/photos")
public class PhotoController {

    private final PhotoRepository photoRepository;

    private final PhotoSaver photoSaver;

    @Autowired
    public PhotoController(PhotoRepository photoRepository, PhotoSaver photoSaver) {
        this.photoRepository = photoRepository;
        this.photoSaver = photoSaver;
    }

    @RequestMapping(method = RequestMethod.GET)
    Page<Photo> readPhotos(Pageable pageable, UserAuthentication authentication) {
        return photoRepository.findAll(pageable);
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
