package com.example.demo.service.advertisement;

import com.example.demo.entity.advertisement.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeService {
    List<ServiceType> findAll();

    Optional<ServiceType> findById(int id);
}
