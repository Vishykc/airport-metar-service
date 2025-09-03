package com.viktorvranar.airport_metar_service.exception;

public class MetarDataNotFoundException extends RuntimeException {
    
    public MetarDataNotFoundException(String message) {
        super(message);
    }
    
    public MetarDataNotFoundException(String icaoCode, Long id) {
        super("METAR data not found for airport: " + icaoCode + " with ID: " + id);
    }
}