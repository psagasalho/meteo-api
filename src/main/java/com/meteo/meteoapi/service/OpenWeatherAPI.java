package com.meteo.meteoapi.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OpenWeatherAPI {

    @Value("${openweather.token}")
    private String token;
    private final String meteoFiveDaysURI = "https://api.openweathermap.org/data/2.5/forecast";
    private final String fetchCoordinatesURI = "https://api.openweathermap.org/geo/1.0/direct";

    public Map<String, Object> fetchCity(String city, String countryCode){
        Map<String, Object> cityDetails = null;
        String uri = fetchCoordinatesURI + "?q="+city+","+countryCode+"&limit=1&appid="+token;

        List<Map<String, Object>> locationsList = Objects.requireNonNull(
                new RestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }).getBody());

        if(!locationsList.isEmpty()){
            cityDetails = new HashMap<>();
            Map<String,Object> location = locationsList.get(0);
            cityDetails.put("city",location.get("name"));
            cityDetails.put("countryCode",location.get("country"));
            cityDetails.put("lat", location.get("lat"));
            cityDetails.put("lon", location.get("lon"));
        }
        return cityDetails;
    }

    public Map<String, Object> fetchForecastByCoordinates(Double lat, Double lon){
        String uri = meteoFiveDaysURI + "?lat="+lat+"&lon="+lon+"&units=metric&appid="+token;

        Map<String, Object> forecastMap = Objects.requireNonNull(
                new RestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody());

        if(!forecastMap.isEmpty()){
            return forecastMap;
        }
        return null;
    }

}
