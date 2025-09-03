package com.viktorvranar.airport_metar_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Airport METAR Service.
 * This Spring Boot application provides REST endpoints for storing and retrieving
 * METAR (Meteorological Terminal Aviation Routine Weather Report) data for airports.
 */
@SpringBootApplication
public class AirportMetarServiceApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AirportMetarServiceApplication.class, args);
	}

}
