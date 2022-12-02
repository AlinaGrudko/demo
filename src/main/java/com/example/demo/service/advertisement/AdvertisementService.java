package com.example.demo.service.advertisement;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdvertisementService {
    Advertisement save(Advertisement advertisement);

    Page<Advertisement> findPaginatedActiveAds(Pageable pageable);

    Page<Advertisement> findPaginatedInWaitingAds(Pageable pageable);

    Page<Advertisement> findPaginatedNotActiveAds(Pageable pageable);

    Page<Advertisement> findPaginatedAdsToCheck(Pageable pageable);

    Page<Advertisement> findPaginatedSaleAds(Pageable pageable);

    Page<Advertisement> findPaginatedSaleAds(Pageable pageable, AdvertisementDto advertisementDto);

    Page<Advertisement> findPaginatedFreeAds(Pageable pageable);

    Page<Advertisement> findPaginatedFreeAds(Pageable pageable, AdvertisementDto advertisementDto);

    Optional<Advertisement> findById(int id);

    List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus);

    Advertisement update(Advertisement advertisement);

    void deactivateAdvertisement(int id);

    void activateAdvertisement(int id);

    void approveAdvertisement(int id);

    void denyAdvertisement(int id);

    boolean hasAccessAuthorizedUser(Advertisement advertisement);
}
