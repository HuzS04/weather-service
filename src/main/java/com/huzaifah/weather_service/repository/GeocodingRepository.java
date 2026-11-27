package com.huzaifah.weather_service.repository;

import com.huzaifah.weather_service.dto.GeocodingResponse;
import com.huzaifah.weather_service.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j //gives us the log object
@Repository //spring creates and manages this class
@RequiredArgsConstructor //makes constructor for final fields
public class GeocodingRepository {

    private final RestClient geocodingRestClient; //restclient bean from OpenMeteoConfig

    public GeocodingResponse searchCity(String cityName) {
        log.info("Calling Open-Meteo geocoding for city: {}", cityName);

        try {
            GeocodingResponse response = geocodingRestClient.get() //GET request
                    .uri(uriBuilder -> uriBuilder //builds url on top of base url
                            .path("/v1/search")
                            .queryParam("name", cityName)
                            .queryParam("count", 10)
                            .queryParam("countryCode", "GB") //only uk results from api
                            .build())
                    .retrieve() //sends the request
                    .body(GeocodingResponse.class); //turns json into our record

            log.info("Open-Meteo geocoding response for {}: {}", cityName, response);
            return response;

        } catch (RestClientException e) { //api down or returned an error
            log.error("Open-Meteo geocoding call failed for {}", cityName, e);
            throw new ExternalApiException("Failed to call Open-Meteo geocoding API", e); //throw our own exception, keep original inside
        }
    }
}