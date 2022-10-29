package com.meteo.meteoapi.controller;
import com.meteo.meteoapi.model.Location;
import com.meteo.meteoapi.repository.MeteoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Transactional
public class MeteoController {

    private final Logger log = LoggerFactory.getLogger(MeteoController.class);
    private final MeteoRepository meteoRepository;

    public MeteoController(MeteoRepository meteoRepository) {
        this.meteoRepository = meteoRepository;
    }

    @GetMapping("/")
    public Double index() {
        String uri = "http://api.openweathermap.org/geo/1.0/direct?q=Aveiro,PT&limit=1&appid=1427ab33252ab6a1399d833eaf84e908";
        RestTemplate restTemplate = new RestTemplate();
        //String result = restTemplate.getForObject(uri, String.class);
        Double lat = 0D;

        List<Map<String, Object>> locationsList = Objects.requireNonNull(
                restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody());

        if(locationsList!=null){
            Map<String,Object> location = locationsList.get(0);
            lat = (Double) location.get("lat");
        }
        return lat;
    }

    /*Registration screen and list of cities: Allow the user to register only valid cities in the API (which returns data). In the
    list of cities, you should have a link to view the details of the forecast;*/
    @PostMapping("/register_cities")
    public ResponseEntity<Location> createCities(@RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save locations : {}", location);
        if (location.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new location cannot already have an ID");
        }

        String uri = "http://api.openweathermap.org/geo/1.0/direct?q=Aveiro,PT&limit=1&appid=1427ab33252ab6a1399d833eaf84e908";
        RestTemplate restTemplate = new RestTemplate();

        List<Map<String, Object>> locationsList = Objects.requireNonNull(
                restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody());

        Location resultLocation = meteoRepository.save(location);

        return ResponseEntity
                .created(new URI("/register_cities" + resultLocation.getId()))
                .body(resultLocation);
    }

    //Detail screen of the choices: Displays a forecast of 3 or 5 days for the chosen city.
}