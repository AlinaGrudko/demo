package com.example.demo.controller;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.entity.notification.Notification;
import com.example.demo.entity.photo.Photo;
import com.example.demo.mapper.AdvertisementMapper;
import com.example.demo.service.advertisement.AdvertisementService;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import com.example.demo.service.advertisement.ServiceTypeService;
import com.example.demo.service.notification.NotificationService;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Setter
@RequestMapping("/user")
public class UserController {

    private static final String ATTRIBUTE_NAME = "advertisementDto";
    private static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

    @Value("${upload.path}")
    private String uploadPath;

    private AdvertisementTypeService advertisementTypeService;

    private ServiceTypeService serviceTypeService;

    private AdvertisementService advertisementService;

    private AdvertisementMapper advertisementMapper;

    private UserService userService;

    private NotificationService notificationService;

    @GetMapping("/active-ads")
    public String showActiveAds(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage = this.advertisementService.findPaginatedActiveAds(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/active-ads";
    }

    @GetMapping("/in-review-ads")
    public String showInReviewAds(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage = this.advertisementService.findPaginatedInWaitingAds(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/in-review-ads";
    }

    @GetMapping("/not-active-ads")
    public String showNotActiveAds(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage = this.advertisementService.findPaginatedNotActiveAds(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/not-active-ads";
    }

    @GetMapping("/favorites-ads")
    public String showFavoritesAds(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Advertisement> advertisementPage = this.userService.findPaginatedFavoriteAdvertisements(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("advertisementPage", advertisementPage);

        int totalPages = advertisementPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/favorites-ads";
    }

    @GetMapping("/ad/add")
    public String addAd(Model model) {
        List<ServiceType> serviceTypes = this.serviceTypeService.findAll();
        List<AdvertisementType> advertisementTypes = this.advertisementTypeService.findAll();

        model.addAttribute("serviceTypes", serviceTypes);
        model.addAttribute("advertisementTypes", advertisementTypes);

        if(!model.containsAttribute(BINDING_RESULT_NAME)) {
            AdvertisementDto advertisementDto = new AdvertisementDto();
            advertisementDto.setPetAge("0");
            model.addAttribute(ATTRIBUTE_NAME, advertisementDto);
        }
        return "user/ad/add";
    }

    @PostMapping("/ad/add")
    public String addAd(@RequestParam("files") List<MultipartFile> files,
                        @ModelAttribute
                        @Valid AdvertisementDto advertisementDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) throws IOException {

        if((files.size() <= 1 && files.get(0).getOriginalFilename().isEmpty()) || files.size() > 10) {
            bindingResult.rejectValue("photos", "error.ad.advertisementPhotos", "*Фотографий питомца должно быть от 1 до 10 включительно");
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, advertisementDto);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
            return "redirect:/user/ad/add";
        }

        List<Photo> photoList = savePhotos(files);
        advertisementDto.setPhotos(photoList);

        Advertisement advertisement = this.advertisementMapper.advertisementDtoToAdvertisement(advertisementDto);
        this.advertisementService.save(advertisement);

        redirectAttributes.addFlashAttribute("successMessage", "Ваше объявление подано на проверку!");

        return "redirect:/";
    }

    private List<Photo> savePhotos(List<MultipartFile> photos) throws IOException {
        List<Photo> photoList = new ArrayList<>();

        for (MultipartFile photo : photos) {
            if (photo.getContentType().equals("image/png") || photo.getContentType().equals("image/jpeg")) {
                File uploadDir = new File(uploadPath + "/advertisement");

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + photo.getOriginalFilename();
                photo.transferTo(new File(uploadPath + "/advertisement/" + resultFileName));

                Photo advertisementPhoto = new Photo();
                advertisementPhoto.setName(resultFileName);
                photoList.add(advertisementPhoto);
            }
        }
        return photoList;
    }

    @GetMapping("/notification")
    public String showNotifications(Model model,
                                    @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Notification> notificationPage = this.notificationService.findPaginatedNotifications(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("notificationPage", notificationPage);

        int totalPages = notificationPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "notification/notification";
    }

    @PostMapping("/notification/read")
    public String readNotification(@RequestParam int id,
                                   @RequestParam String url) {

        this.notificationService.readNotification(id);

        return "redirect:" + url;
    }

    @PostMapping("/notification/read/all")
    public String readAllNotifications(@RequestParam String url) {

        this.notificationService.readAllNotifications();

        return "redirect:" + url;
    }

    @PostMapping("/notification/delete")
    public String deleteNotification(@RequestParam int id,
                                     @RequestParam String url) {

        this.notificationService.deleteNotification(id);

        return "redirect:" + url;
    }

    @PostMapping("/notification/delete/all")
    public String deleteAllNotifications(@RequestParam String url) {

        this.notificationService.deleteAllNotifications();

        return "redirect:" + url;
    }
}
