package com.example.demo.dto.advertisement;
;
import com.example.demo.entity.advertisement.AdvertisementStatus;
import com.example.demo.entity.photo.Photo;
import com.example.demo.entity.user.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.List;

@Data
public class AdvertisementDto {
    private int id;
    private int serviceTypeId;
    private int advertisementTypeId;

    private List<Photo> photos;

    private User user;

    @NotBlank(message = "*Пожалуйста, введите название города")
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
    private String city;

    @NotBlank(message = "*Пожалуйста, введите название района")
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
    private String district;

    @NotBlank(message = "*Пожалуйста, введите название породы")
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\ \\-\\.]+$", message = "*Пожалуйста, используйте только A-Z, А-Я, пробел, \"-\" и \".\"")
    private String breed;

    @NotBlank(message = "*Пожалуйста, введите пол")
    @Pattern(regexp = "^[0-9A-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, используйте только 0-9, A-Z, А-Я и \"-\" для номера дома")
    private String sex;

    @NotBlank(message = "*Пожалуйста, введите окрас")
    @Pattern(regexp = "^[0-9A-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, используйте только 0-9, A-Z, А-Я и \"-\" для номера дома")
    private String color;

    @NotBlank(message = "*Пожалуйста, введите возраст")
    @Pattern(regexp = "^[0-9A-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, используйте только 0-9, A-Z, А-Я и \"-\" для номера дома")
    private String petAge;

    @Min(value = 0, message = "*Стоимость не может быть отрицательной")
    private double price;

    private double priceFrom;

    private double priceTo;

    @Size(min = 5, max = 100, message = "*Описание должно содержать от 5 до 100 символов включительно")
    private String description;

    private AdvertisementStatus status;
}
