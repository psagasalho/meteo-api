package com.meteo.meteoapi.controller;
import com.meteo.meteoapi.model.Location;
import com.meteo.meteoapi.model.LocationIdAndName;
import com.meteo.meteoapi.repository.MeteoRepository;
import com.meteo.meteoapi.service.OpenWeatherAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
public class MeteoController {

    private final MeteoRepository meteoRepository;
    private final OpenWeatherAPI openWeatherAPI;
    private final Logger log = LoggerFactory.getLogger(MeteoController.class);

    public MeteoController(MeteoRepository meteoRepository, OpenWeatherAPI openWeatherAPI) {
        this.meteoRepository = meteoRepository;
        this.openWeatherAPI = openWeatherAPI;
    }

    /*Registration screen and list of cities: Allow the user to register only valid cities in the API (which returns data). In the
    list of cities, you should have a link to view the details of the forecast;*/
    @PostMapping("/cities/register")
    public ResponseEntity<Location> createCities(@RequestBody Location location) {
        log.debug("REST request to save locations : {}", location);
        List<Location> findByCityAndCountryCode = meteoRepository.findAllByCityAndCountry(location.getCity(), location.getCountry());
        if (!findByCityAndCountryCode.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate City!");
        }

        if (location.getCity() == null || location.getCountry() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid City");
        }

        Map<String, Object> cityAPI = openWeatherAPI.fetchCity(location.getCity(), location.getCountry());
        if(cityAPI != null){
            location.setCity((String)cityAPI.get("city"));
            location.setCountry((String) cityAPI.get("countryCode"));
            location.setLat((Double)cityAPI.get("lat"));
            location.setLon((Double)cityAPI.get("lon"));
            Location resultLocation = meteoRepository.save(location);
            return ResponseEntity.ok(resultLocation);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City Not Found!");
    }

    @GetMapping("/cities/all")
    public List<LocationIdAndName> citiesList(){
        return meteoRepository.findAllBy();
    }

    //Detail screen of the choices: Displays a forecast of 3 or 5 days for the chosen city.
    @GetMapping("/cities/forecast/{id}")
    public Map<String,Object> getForecastOfCity(@PathVariable Long id) {
        log.debug("REST request to get RestaurantsAndBar : {}", id);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid City");
        }

        List<Location> findById = meteoRepository.findAllById(id);
        if (!findById.isEmpty()){
            return openWeatherAPI.fetchForecastByCoordinates(findById.get(0).getLat(), findById.get(0).getLon());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forecast Not Found!");
    }
}