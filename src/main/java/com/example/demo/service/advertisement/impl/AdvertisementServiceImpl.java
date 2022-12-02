package com.example.demo.service.advertisement.impl;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementStatus;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.entity.notification.Notification;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.repository.AdvertisementRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.advertisement.AdvertisementService;
import com.example.demo.service.advertisement.ServiceTypeService;
import com.example.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@Transactional
public class AdvertisementServiceImpl implements AdvertisementService {

    private AdvertisementRepository advertisementRepository;

    private UserService userService;

    private RoleRepository roleRepository;

    private ServiceTypeService serviceTypeService;

    @Override
    public Advertisement save(Advertisement advertisement) {
        advertisement.setStatus(AdvertisementStatus.IN_WAITING);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());
        advertisement.setUser(user);

        return advertisementRepository.save(advertisement);
    }

    @Override
    public Page<Advertisement> findPaginatedActiveAds(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        List<Advertisement> properties = this.advertisementRepository.findByStatusAndUser(AdvertisementStatus.APPROVED, user);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());
    }

    @Override
    public Page<Advertisement> findPaginatedInWaitingAds(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        List<Advertisement> properties = this.advertisementRepository.findByStatusAndUser(AdvertisementStatus.IN_WAITING, user);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Page<Advertisement> findPaginatedNotActiveAds(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        List<Advertisement> properties = this.advertisementRepository.findByStatusAndUser(AdvertisementStatus.DENIED, user);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Page<Advertisement> findPaginatedAdsToCheck(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Advertisement> properties = this.advertisementRepository.findByStatus(AdvertisementStatus.IN_WAITING);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Page<Advertisement> findPaginatedSaleAds(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(1);

        List<Advertisement> properties = this.advertisementRepository.findByStatusAndServiceType(AdvertisementStatus.APPROVED, serviceTypeOptional.orElse(null));
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());
    }

    @Override
    public Page<Advertisement> findPaginatedSaleAds(Pageable pageable, AdvertisementDto advertisementDto) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Advertisement> properties = this.advertisementRepository.findAdsByAdvertisementDto(advertisementDto, 1);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Page<Advertisement> findPaginatedFreeAds(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(2);

        List<Advertisement> properties = this.advertisementRepository.findByStatusAndServiceType(AdvertisementStatus.APPROVED, serviceTypeOptional.orElse(null));
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Page<Advertisement> findPaginatedFreeAds(Pageable pageable, AdvertisementDto advertisementDto) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Advertisement> properties = this.advertisementRepository.findAdsByAdvertisementDto(advertisementDto, 2);
        List<Advertisement> resultList;

        if (properties.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, properties.size());
            resultList = properties.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());

    }

    @Override
    public Optional<Advertisement> findById(int id) {
        return this.advertisementRepository.findById(id);
    }

    @Override
    public List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus) {
        return this.advertisementRepository.findByStatus(advertisementStatus);
    }

    @Override
    public Advertisement update(Advertisement advertisement) {
        advertisement.setStatus(AdvertisementStatus.IN_WAITING);
        return this.advertisementRepository.save(advertisement);
    }

    @Override
    public void deactivateAdvertisement(int id) {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(id);
        advertisementOptional.ifPresent(advertisement -> advertisement.setStatus(AdvertisementStatus.DENIED));
    }

    @Override
    public void activateAdvertisement(int id) {
        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(id);
        advertisementOptional.ifPresent(advertisement -> advertisement.setStatus(AdvertisementStatus.IN_WAITING));

    }

    @Override
    public void approveAdvertisement(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(id);
        advertisementOptional.ifPresent(advertisement -> {
            advertisement.setStatus(AdvertisementStatus.APPROVED);
            Notification notification = new Notification();
            notification.setActive(true);
            notification.setUserFrom(user);
            StringBuilder sb = new StringBuilder("Ваше объявление: ")
                    .append(advertisement.getAdvertisementType().getName()).append(", ")
                    .append("г. ").append(advertisement.getCity()).append(", ")
                    .append("р-н ").append(advertisement.getDistrict()).append(", ")
                    .append("порода ").append(advertisement.getBreed());
            sb.append("\nбыло одобрено!");
            notification.setMessage(sb.toString());
            notification.setDateTime(LocalDateTime.now());

            User advertisementUser = advertisement.getUser();
            advertisementUser.getNotifications().add(notification);
        });
    }

    @Override
    public void denyAdvertisement(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        Optional<Advertisement> advertisementOptional = this.advertisementRepository.findById(id);
        advertisementOptional.ifPresent(advertisement -> {
            advertisement.setStatus(AdvertisementStatus.DENIED);
            Notification notification = new Notification();
            notification.setActive(true);
            notification.setUserFrom(user);
            StringBuilder sb = new StringBuilder("Ваше объявление: ")
                    .append(advertisement.getAdvertisementType().getName()).append(", ")
                    .append("г. ").append(advertisement.getCity()).append(", ")
                    .append("р-н ").append(advertisement.getDistrict()).append(", ")
                    .append("порода ").append(advertisement.getBreed());
            sb.append("\nбыло отклонено!");
            notification.setMessage(sb.toString());
            notification.setDateTime(LocalDateTime.now());

            User propertyUser = advertisement.getUser();
            propertyUser.getNotifications().add(notification);
        });
    }

    @Override
    public boolean hasAccessAuthorizedUser(Advertisement advertisement) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        Role moderatorRole = this.roleRepository.findByRole("ROLE_MODERATOR");

        switch (advertisement.getStatus()) {

            case IN_WAITING: {
                if (advertisement.getUser().equals(user) || user.getRoles().contains(moderatorRole)) {
                    return true;
                }
            }
            break;

            case APPROVED:
                return true;

            case DENIED: {
                if (advertisement.getUser().equals(user)) {
                    return true;
                }
            }
            break;
        }

        return false;
    }
}
