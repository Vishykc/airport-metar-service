package com.viktorvranar.airport_metar_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.service.MetarService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * REST controller for managing METAR (Meteorological Terminal Aviation Routine Weather Report) data.
 * Provides endpoints for storing and retrieving METAR data for airports.
 */
/**
 * REST controller for managing METAR data.
 * Provides endpoints for storing and retrieving METAR data for airports.
 */
@RestController
@RequestMapping("/airport")
public class MetarController {
    
    private static final Logger logger = LoggerFactory.getLogger(MetarController.class);
    
    private final MetarService metarService;
    
    public MetarController(MetarService metarService) {
        this.metarService = metarService;
    }
    
    /**
     * Store METAR data for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @param request the request containing METAR data
     * @return ResponseEntity with the saved METAR data
     */
    @PostMapping("/{icaoCode}/METAR")
    public ResponseEntity<MetarData> storeMetarData(
            @PathVariable String icaoCode,
            @Valid @RequestBody MetarDataRequest request) {
        
        logger.info("Storing METAR data for airport: {}", icaoCode);
        MetarData metarData = metarService.saveMetarData(icaoCode, request.getData());
        logger.info("Successfully stored METAR data for airport: {} with ID: {}", icaoCode, metarData.getId());
        return new ResponseEntity<>(metarData, HttpStatus.CREATED);
    }
    
    /**
     * Retrieve the latest METAR data for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @return ResponseEntity with the latest METAR data or NOT_FOUND if no data exists
     */
    @GetMapping("/{icaoCode}/METAR")
    public ResponseEntity<MetarData> getLatestMetarData(@PathVariable String icaoCode) {
        logger.info("Retrieving latest METAR data for airport: {}", icaoCode);
        try {
            MetarData metarData = metarService.getLatestMetarData(icaoCode);
            logger.info("Successfully retrieved METAR data for airport: {} with ID: {}", icaoCode, metarData.getId());
            return new ResponseEntity<>(metarData, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("No METAR data found for airport: {}", icaoCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * DTO class for METAR data request body.
     */
    public static class MetarDataRequest {
        @NotBlank(message = "METAR data cannot be blank")
        private String data;
        
        public String getData() {
            return data;
        }
        
        public void setData(String data) {
            this.data = data;
        }
    }
}
