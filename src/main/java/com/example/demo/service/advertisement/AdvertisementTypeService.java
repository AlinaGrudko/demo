package com.example.demo.service.advertisement;

import com.example.demo.entity.advertisement.AdvertisementType;

import java.util.List;
import java.util.Optional;

public interface AdvertisementTypeService {

    List<AdvertisementType> findAll();

    Optional<AdvertisementType> findById(int id);
}
