package com.example.demo.controller;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.service.advertisement.AdvertisementService;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Setter
@RequestMapping("/catalog")
public class CatalogController {

    private AdvertisementService advertisementService;

    private AdvertisementTypeService advertisementTypeService;

    @GetMapping("/sale")
    public String showSaleCatalog(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @ModelAttribute AdvertisementDto advertisementDto) {

        List<AdvertisementType> advertisementTypes = this.advertisementTypeService.findAll();
        model.addAttribute("advertisementTypes", advertisementTypes);
        model.addAttribute("advertisementDto", new AdvertisementDto());

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage;
        if(advertisementDto.getAdvertisementTypeId() != 0) {
            advertisementPage = this.advertisementService.findPaginatedSaleAds(
                    PageRequest.of(currentPage - 1, pageSize),
                    advertisementDto);
            model.addAttribute("advertisementDto", advertisementDto);
        } else  {
            advertisementPage = this.advertisementService.findPaginatedSaleAds(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "catalog/sale";
    }

    @GetMapping("/free")
    public String showFreeCatalog(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @ModelAttribute AdvertisementDto advertisementDto) {

        List<AdvertisementType> advertisementTypes = this.advertisementTypeService.findAll();
        model.addAttribute("advertisementTypes", advertisementTypes);
        model.addAttribute("advertisementDto", new AdvertisementDto());

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage;
        if(advertisementDto.getAdvertisementTypeId() != 0) {
            advertisementPage = this.advertisementService.findPaginatedFreeAds(
                    PageRequest.of(currentPage - 1, pageSize),
                    advertisementDto);
            model.addAttribute("advertisementDto", advertisementDto);
        } else  {
            advertisementPage = this.advertisementService.findPaginatedFreeAds(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "catalog/free";
    }
}
