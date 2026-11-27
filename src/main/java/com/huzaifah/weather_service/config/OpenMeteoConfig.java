package com.huzaifah.weather_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration //marks this class as setup
public class OpenMeteoConfig {

    @Bean //run this method at the startup and keep the RestClient it returns
    public RestClient geocodingRestClient(
            @Value("${open-meteo.geocoding.base-url}") String baseUrl) { //passes base URL from YAML into the method
        return RestClient.builder() //build RestClient with open-meteo address saved
                .baseUrl(baseUrl)
                .build();
    }
}
