package com.huzaifah.weather_service.service;

import com.huzaifah.weather_service.dto.GeocodingResponse;
import com.huzaifah.weather_service.dto.GeocodingResult;
import com.huzaifah.weather_service.exception.ExternalApiException;
import com.huzaifah.weather_service.exception.UnknownCityException;
import com.huzaifah.weather_service.repository.GeocodingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //turns on mockito for this test class
class GeocodingServiceTest {

    @Mock //fake repository
    private GeocodingRepository repository;

    @InjectMocks //real service, with the fake repo put inside it
    private GeocodingService service;

    @Test
    void returnsFirstUkResult() {
        //setup what the fake repo should return
        GeocodingResult manchester = new GeocodingResult("Manchester", 53.48, -2.24, "GB");
        GeocodingResult airport = new GeocodingResult("Manchester Airport", 53.35, -2.27, "GB");
        when(repository.searchCity("Manchester"))
                .thenReturn(new GeocodingResponse(List.of(manchester, airport))); //when someone calls method, return response with these two results in order

        //call the real method
        GeocodingResult result = service.resolveCoordinates("Manchester");

        //check the answer
        assertEquals(manchester, result);
    }

    @Test
    void skipsNonUkResults() {
        //non uk result first, then uk
        GeocodingResult manchester = new GeocodingResult("Manchester", 53.48, -2.24, "GB");
        GeocodingResult paris = new GeocodingResult("Paris", 48.86, 2.35, "FR"); //non UK result
        when(repository.searchCity("Manchester"))
                .thenReturn(new GeocodingResponse(List.of(paris, manchester))); //paris first so filter has to skip it

        GeocodingResult result = service.resolveCoordinates("Manchester"); //call real method

        assertEquals(manchester, result); //should get manchester not paris
    }

    @Test
    void throwsUnknownCity_whenOnlyNonUkResults() {
        //only non UK results
        GeocodingResult paris = new GeocodingResult("Paris", 48.86, 2.35, "FR");
        GeocodingResult barcelona = new GeocodingResult("Barcelona", 41.39, 2.16, "ES");
        when(repository.searchCity("Manchester"))
                .thenReturn(new GeocodingResponse(List.of(paris, barcelona))); //fake repo returns these, service filter removes both so nothing left

        //method should throw error, not return anything
        assertThrows(UnknownCityException.class, //the exception expected to be thrown
                () -> service.resolveCoordinates("Manchester")); //code to run
    }

    @Test
    void throwsUnknownCity_whenResultsNull() {
        when(repository.searchCity("Manchester"))
                .thenReturn(new GeocodingResponse(null)); //no list

        assertThrows(UnknownCityException.class, //the exception expected to be thrown
                () -> service.resolveCoordinates("Manchester")); //code to run
    }

    @Test
    void throwsUnknownCity_whenResultsEmpty() {
        when(repository.searchCity("Manchester"))
                .thenReturn(new GeocodingResponse(List.of())); //empty list

        assertThrows(UnknownCityException.class, //the exception expected to be thrown
                () -> service.resolveCoordinates("Manchester")); //code to run
    }

    @Test
    void throwsIllegalArgument_whenNameBlank() {
        //blank name so it fails at the name check, no fake repo setup needed
        assertThrows(IllegalArgumentException.class, //the exception expected to be thrown
                () -> service.resolveCoordinates(" ")); //spaces only, tests isBlank
        verify(repository, never()).searchCity(any()); //repo should never be called
    }

    @Test
    void passesThroughExternalApiException() {
        //fake repo throws like the api is down
        when(repository.searchCity("Manchester"))
                .thenThrow(new ExternalApiException("API down", null)); //exception object goes inside thenThrow

        //service doesnt catch it so it should come straight through
        assertThrows(ExternalApiException.class, //the exception expected to be thrown
                () -> service.resolveCoordinates("Manchester")); //code to run
    }
}