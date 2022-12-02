package com.example.demo.entity.advertisement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "advertisement_type")
public class AdvertisementType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_type_id")
    private int id;

    @Column(name = "name")
    private String name;
}
