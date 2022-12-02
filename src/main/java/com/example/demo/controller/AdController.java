package com.example.demo.controller;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.entity.advertisement.ServiceType;
import com.example.demo.entity.photo.Photo;
import com.example.demo.mapper.AdvertisementMapper;
import com.example.demo.service.advertisement.AdvertisementService;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import com.example.demo.service.advertisement.ServiceTypeService;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Setter
@SessionAttributes(AdController.ATTRIBUTE_NAME)
public class AdController {
    public static final String ATTRIBUTE_NAME = "advertisementDto";

    public static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

    @Value("${upload.path}")
    private String uploadPath;

    private AdvertisementService advertisementService;

    private UserService userService;

    private AdvertisementMapper advertisementMapper;

    private ServiceTypeService serviceTypeService;

    private AdvertisementTypeService advertisementTypeService;

    @GetMapping("/ad/{id}")
    public String showAd(@PathVariable int id,
                         Model model) {

        Optional<Advertisement> advertisementOptional = this.advertisementService.findById(id);

        if(advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();
            if(advertisementService.hasAccessAuthorizedUser(advertisement)) {
                model.addAttribute("advertisement", advertisement);
                return "ad/ad";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/user/add/favorite")
    public String addFavoriteAdvertisement(@RequestParam int id,
                                      @RequestParam String url) {

        this.userService.addFavoriteAdvertisement(id);
        return "redirect:" + url;
    }

    @PostMapping("/user/delete/favorite")
    public String deleteFavoriteAdvertisement(@RequestParam int id,
                                         @RequestParam String url) {

        this.userService.deleteFavoriteAdvertisement(id);
        return "redirect:" + url;
    }

    @GetMapping("/user/ad/edit/{id}")
    public String editAd(@PathVariable int id,
                         @RequestParam("url") Optional<String> urlOptional,
                         Model model) {

        List<ServiceType> serviceTypes = this.serviceTypeService.findAll();
        List<AdvertisementType> advertisementTypes = this.advertisementTypeService.findAll();
        model.addAttribute("serviceTypes", serviceTypes);
        model.addAttribute("advertisementTypes", advertisementTypes);

        Optional<Advertisement> advertisementOptional = this.advertisementService.findById(id);

        if(!model.containsAttribute(BINDING_RESULT_NAME)) {
            if(advertisementOptional.isEmpty()) {
                return "error";
            }

            String url = urlOptional.orElse("/ad/" + id);
            model.addAttribute("url", url);

            AdvertisementDto advertisementDto = advertisementMapper.advertisementToAdvertisementDto(advertisementOptional.get());
            model.addAttribute(ATTRIBUTE_NAME, advertisementDto);
        }

        return "user/ad/edit";
    }

    @PostMapping("/user/ad/edit")
    public String editAd(@RequestParam("files") List<MultipartFile> files,
                         @RequestParam String url,
                         @ModelAttribute
                             @Valid AdvertisementDto advertisementDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         SessionStatus sessionStatus) throws IOException {

        long size = files.stream().filter(file -> !file.getOriginalFilename().isEmpty()).count();
        if(size + advertisementDto.getPhotos().size() > 10) {
            bindingResult.rejectValue("photos", "error.property.photos", "*Всего фотографий должно быть от 1 до 10 включительно");
        }

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
            redirectAttributes.addFlashAttribute("url", url);
            return "redirect:/user/ad/edit/" + advertisementDto.getId();
        }

        List<Photo> photos = savePhotos(files);
        advertisementDto.getPhotos().addAll(photos);
        Advertisement advertisement = this.advertisementMapper.advertisementDtoToAdvertisement(advertisementDto);
        this.advertisementService.update(advertisement);
        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("successMessage", "Информация о питомце успешно обновлена!");

        return "redirect:" + url;
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

    @PostMapping("/user/ad/deactivate")
    public String deactivateAd(@RequestParam int id,
                               @RequestParam String url) {

        this.advertisementService.deactivateAdvertisement(id);

        return "redirect:" + url;
    }

    @PostMapping("/user/ad/activate")
    public String activateAd(@RequestParam int id,
                             @RequestParam String url) {

        this.advertisementService.activateAdvertisement(id);

        return "redirect:" + url;
    }
}
