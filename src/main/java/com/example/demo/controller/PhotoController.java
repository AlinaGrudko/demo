package com.example.demo.controller;

import com.example.demo.entity.photo.Photo;
import com.example.demo.service.photo.PhotoService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Optional;

@Controller
@Setter
@RequestMapping("/photo")
public class PhotoController {

    @Value("${upload.path}")
    private String uploadPath;

    private PhotoService photoService;

    @PostMapping("/delete")
    public String deleteAdvertisementPhoto(@RequestParam int id,
                                      @RequestParam String url,
                                      Model model) {
        Optional<Photo> photoOptional = this.photoService.findById(id);

        photoOptional.ifPresent(photo -> {
            this.photoService.delete(id);
            File filePhoto = new File(uploadPath + "/advertisement/" + photo.getName());
            filePhoto.delete();
        });

        return "redirect:" + url;
    }
}
