package com.example.demo.repository;

import com.example.demo.entity.advertisement.AdvertisementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementTypeRepository extends JpaRepository<AdvertisementType, Integer> {
}
