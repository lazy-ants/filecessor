package com.lazyants.filecessor.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {
    Page<Photo> findByUserId(Pageable pageable, String userId);
}