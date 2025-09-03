package com.viktorvranar.airport_metar_service.exception;

/**
 * Exception thrown when METAR data is not found for a given airport.
 */
public class MetarDataNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new MetarDataNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public MetarDataNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new MetarDataNotFoundException with the specified ICAO code and ID.
     *
     * @param icaoCode the ICAO code of the airport
     * @param id the ID of the METAR data
     */
    public MetarDataNotFoundException(String icaoCode, Long id) {
        super("METAR data not found for airport: " + icaoCode + " with ID: " + id);
    }
}