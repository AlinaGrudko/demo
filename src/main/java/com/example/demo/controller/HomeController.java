package com.example.demo.controller;

import com.example.demo.dto.advertisement.AdvertisementDto;
import com.example.demo.entity.advertisement.AdvertisementType;
import com.example.demo.service.advertisement.AdvertisementTypeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Setter
public class HomeController {

    private AdvertisementTypeService advertisementTypeService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");

        List<AdvertisementType> advertisementTypes = this.advertisementTypeService.findAll();
        modelAndView.addObject("advertisementTypes", advertisementTypes);
        modelAndView.addObject("advertisementDto", new AdvertisementDto());

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView("about");
        return modelAndView;
    }

    @GetMapping("/services")
    public ModelAndView services() {
        ModelAndView modelAndView = new ModelAndView("services");
        return modelAndView;
    }

    @GetMapping("/expired")
    public ModelAndView expiredError() {
        ModelAndView modelAndView = new ModelAndView("expired-error");
        return modelAndView;
    }
}
