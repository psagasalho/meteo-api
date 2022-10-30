package com.meteo.meteoapi.repository;

import com.meteo.meteoapi.model.Location;
import com.meteo.meteoapi.model.LocationIdAndName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeteoRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByCityAndCountry(String city, String country);

    List<LocationIdAndName> findAllBy();

    List<Location> findAllById(Long id);
}
