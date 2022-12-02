package com.example.demo.service.advertisement.impl;

import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.repository.ServiceTypeRepository;
import com.example.demo.service.advertisement.ServiceTypeService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Transactional
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<ServiceType> findAll() {
        return this.serviceTypeRepository.findAll();
    }

    @Override
    public Optional<ServiceType> findById(int id) {
        return this.serviceTypeRepository.findById(id);
    }
}
