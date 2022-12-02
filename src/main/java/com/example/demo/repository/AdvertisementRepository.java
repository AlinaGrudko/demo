package com.example.demo.repository;


import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementStatus;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer>, AdvertisementSearchRepository {

    List<Advertisement> findByStatusAndUser(AdvertisementStatus advertisementStatus, User user);

    List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus);

    List<Advertisement> findByStatusAndServiceType(AdvertisementStatus advertisementStatus, ServiceType serviceType);
}
