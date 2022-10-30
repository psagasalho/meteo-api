package com.meteo.meteoapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "location")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;
}
