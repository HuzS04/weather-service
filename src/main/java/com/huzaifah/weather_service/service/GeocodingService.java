package com.huzaifah.weather_service.service;

import com.huzaifah.weather_service.dto.GeocodingResponse;
import com.huzaifah.weather_service.dto.GeocodingResult;
import com.huzaifah.weather_service.exception.UnknownCityException;
import com.huzaifah.weather_service.repository.GeocodingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j //gives us the log object
@RequiredArgsConstructor //makes constructor for final fields
@Service //spring creates and manages this class, holds business logic
public class GeocodingService {

    private static final String UK_COUNTRY_CODE = "GB"; //constant for uk check
    private final GeocodingRepository repository; //repo that calls the api

    public GeocodingResult resolveCoordinates(String cityName) {
        log.info("Resolving coordinates for city: {}", cityName);

        if (cityName == null || cityName.isBlank()) { //nothing to search
            throw new IllegalArgumentException("Must enter a city name");
        }

        GeocodingResponse response = repository.searchCity(cityName);

        if (response == null || response.results() == null || response.results().isEmpty()) { //null check first so no crash
            throw new UnknownCityException(cityName);
        }

        GeocodingResult result = response.results().stream()
                .filter(r -> UK_COUNTRY_CODE.equals(r.countryCode())) //keep uk results only, constant first so null is safe
                .findFirst() //take the top result
                .orElseThrow(() -> new UnknownCityException(cityName)); //no uk result left

        log.info("Resolved {} to lat={}, lon={}", cityName, result.latitude(), result.longitude());
        return result;
    }
}