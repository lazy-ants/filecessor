package com.lazyants.filecessor.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "photos")
public interface PhotoRepository extends MongoRepository<Photo, String> {}