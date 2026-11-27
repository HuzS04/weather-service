package com.huzaifah.weather_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) //ignore JSON fields this class doesnt have
public record GeocodingResult(
        String name,
        double latitude,
        double longitude,
        @JsonProperty("country_code") String countryCode //JSON field "country_code" goes into field countryCode
) {
}
