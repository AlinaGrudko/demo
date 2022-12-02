package com.example.demo.repository;

import com.example.demo.entity.advertisement.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository <ServiceType, Integer> {
}
