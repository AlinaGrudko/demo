package com.example.demo.service.photo;

import com.example.demo.entity.photo.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    Optional<Photo> findById(int id);

    void delete(int id);

    Photo save(Photo photo);

    List<Photo> saveAll(List<Photo> photos);
}
