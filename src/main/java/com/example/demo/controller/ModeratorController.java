package com.example.demo.controller;

import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.service.advertisement.AdvertisementService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Setter
@RequestMapping("/moderator")
public class ModeratorController {

    private AdvertisementService advertisementService;

    @GetMapping("/check-ads")
    public String checkAds(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage = this.advertisementService.findPaginatedAdsToCheck(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "moderator/check-ads";
    }

    @PostMapping("/ad/approve")
    public String approveAd(@RequestParam int id,
                            @RequestParam String url) {

        this.advertisementService.approveAdvertisement(id);

        return "redirect:" + url;
    }

    @PostMapping("/ad/deny")
    public String denyAd(@RequestParam int id,
                         @RequestParam String url) {

        this.advertisementService.denyAdvertisement(id);

        return "redirect:" + url;
    }
}
