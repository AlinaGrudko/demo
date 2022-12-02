package com.example.demo.repository.impl;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementStatus;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.repository.AdvertisementSearchRepository;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import com.example.demo.service.advertisement.ServiceTypeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AdvertisementSearchRepositoryImpl implements AdvertisementSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private AdvertisementTypeService advertisementTypeService;

    @Override
    public List<Advertisement> findAdsByAdvertisementDto(AdvertisementDto advertisementDto, int serviceTypeId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advertisement> query = cb.createQuery(Advertisement.class);
        Root<Advertisement> root = query.from(Advertisement.class);

        List<Predicate> predicates = new ArrayList<>();

        Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(serviceTypeId);
        serviceTypeOptional.ifPresent(serviceType -> predicates.add(cb.equal(root.get("serviceType"), serviceType)));

        predicates.add(cb.equal(root.get("status"), AdvertisementStatus.APPROVED));

        Optional<AdvertisementType> advertisementTypeOptional = this.advertisementTypeService.findById(advertisementDto.getAdvertisementTypeId());
        advertisementTypeOptional.ifPresent(advertisementType -> {
            if (advertisementDto.getAdvertisementTypeId() != 1) {
                predicates.add(cb.equal(root.get("advertisementType"), advertisementType));
            }
        });


        if (advertisementDto.getCity() != null && !advertisementDto.getCity().isEmpty()) {
            predicates.add(cb.like(root.get("city"), "%" + advertisementDto.getCity() + "%"));
        }

        if (advertisementDto.getDistrict() != null && !advertisementDto.getDistrict().isEmpty()) {
            predicates.add(cb.like(root.get("district"), "%" + advertisementDto.getDistrict() + "%"));
        }

        if (advertisementDto.getBreed() != null && !advertisementDto.getBreed().isEmpty()) {
            predicates.add(cb.like(root.get("breed"), "%" + advertisementDto.getBreed() + "%"));
        }

        if (advertisementDto.getSex() != null && !advertisementDto.getSex().isEmpty()) {
            predicates.add(cb.like(root.get("sex"), "%" + advertisementDto.getSex() + "%"));
        }

        if (advertisementDto.getColor() != null && !advertisementDto.getColor().isEmpty()) {
            predicates.add(cb.like(root.get("color"), "%" + advertisementDto.getColor() + "%"));
        }

        int priceFrom = (int) advertisementDto.getPriceFrom() * 100;
        int priceTo = (int) advertisementDto.getPriceTo() * 100;

        if (priceFrom != 0 || priceTo != 0) {
            predicates.add(cb.between(root.get("price"), priceFrom, priceTo));
        }


        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        //сделать отдельную страницу для поиска и проверить
        return entityManager.createQuery(query).getResultList();
    }


}
