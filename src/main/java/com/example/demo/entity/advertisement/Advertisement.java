package com.example.demo.entity.advertisement;

import com.example.demo.entity.photo.Photo;
import com.example.demo.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Table(name = "advertisement")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "advertisement_id")
    private List<Photo> photos;

    @OneToOne
    @JoinColumn(name = "advertisement_type_id")
    private AdvertisementType advertisementType;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "breed")
    private String breed;

    @Column(name = "sex")
    private String sex;

    @Column(name = "pet_age")
    private double petAge;

    @Column(name = "color")
    private String color;

    @Column(name = "pedigree")
    private boolean pedigree;

    @Column(name = "vaccinations")
    private boolean vaccinations;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private AdvertisementStatus status;
}
