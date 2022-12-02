package com.example.demo.repository;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;

import java.util.List;

public interface AdvertisementSearchRepository {

    List<Advertisement> findAdsByAdvertisementDto(AdvertisementDto advertisementDto, int serviceTypeId);
}
