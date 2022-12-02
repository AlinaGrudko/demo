package com.example.demo.mapper;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.repository.AdvertisementTypeRepository;
import com.example.demo.repository.ServiceTypeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper (componentModel = "spring")
public abstract class AdvertisementMapper {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private AdvertisementTypeRepository advertisementTypeRepository;

    @Mapping(target = "serviceType", source = "serviceTypeId")
    @Mapping(target = "advertisementType", source = "advertisementTypeId")
    public abstract Advertisement advertisementDtoToAdvertisement(AdvertisementDto advertisementDto);

    @Mapping(target = "serviceTypeId", source = "serviceType.id")
    @Mapping(target = "advertisementTypeId", source = "advertisementType.id")
    @Mapping(target = "priceFrom", ignore = true)
    @Mapping(target = "priceTo", ignore = true)
    public abstract AdvertisementDto advertisementToAdvertisementDto(Advertisement advertisement);

    ServiceType mapServiceType(int serviceTypeId) {
        Optional<ServiceType> serviceTypeOptional = this.serviceTypeRepository.findById(serviceTypeId);
        return serviceTypeOptional.get();
    }

    AdvertisementType mapPropertyType(int advertisementTypeId) {
        Optional<AdvertisementType> advertisementTypeOptional = this.advertisementTypeRepository.findById(advertisementTypeId);
        return advertisementTypeOptional.get();
    }

    int mapPrice(double price) {
        return (int) (price * 100.0);
    }

    double mapPrice(int price) {
        return price / 100.0;
    }
}
