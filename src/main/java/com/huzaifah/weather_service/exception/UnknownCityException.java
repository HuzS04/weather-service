package com.huzaifah.weather_service.exception;

public class UnknownCityException extends RuntimeException {

    public UnknownCityException(String cityName) {
        super("No UK location found for city: " + cityName);
    }
}
