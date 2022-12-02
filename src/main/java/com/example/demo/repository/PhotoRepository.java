package com.example.demo.repository;

import com.example.demo.entity.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
