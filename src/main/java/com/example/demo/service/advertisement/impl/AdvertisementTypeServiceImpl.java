package com.example.demo.service.advertisement.impl;

import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.repository.AdvertisementTypeRepository;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Transactional
public class AdvertisementTypeServiceImpl implements AdvertisementTypeService {

    private AdvertisementTypeRepository advertisementTypeRepository;

    @Override
    public List<AdvertisementType> findAll() {
        return this.advertisementTypeRepository.findAll();
    }

    @Override
    public Optional<AdvertisementType> findById(int id) {
        return this.advertisementTypeRepository.findById(id);
    }
}
