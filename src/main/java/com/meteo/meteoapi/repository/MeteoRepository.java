package com.meteo.meteoapi.repository;

import com.meteo.meteoapi.model.Location;
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
    /*@Query(value = "select ID from users WHERE userName = ?1", nativeQuery = true)
    Long getUserIdByUserName(String userName);

    @Query(value = "select l.id from destination d, destination_location dl, location l \n" +
            "where d.id = dl.destination_id AND dl.location_id = l.id \n" +
            "AND d.is_active = true AND l.is_active = true", nativeQuery = true)
    List<Long> getAllLocationByDestination();

    @Query(value = "select l.id from destination d, destination_location dl, location l \n" +
            "where dl.destination_id = ?1 AND dl.location_id = l.id \n" +
            "AND d.is_active = true AND l.is_active = true", nativeQuery = true)
    List<Long> getAllLocationsOfADestination(Long destination_id);

    @Query(value = "select * from activities_event where is_active = ?1 AND location_id in (?2) AND is_approved = true", nativeQuery = true)
    List<Location> getAllByLocation(Boolean isActive, List<Long> cities);

    List<Location> findAllByIsActive(Boolean isActive);*/
}
