package com.viktorvranar.airport_metar_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.service.MetarService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * REST controller for managing METAR (Meteorological Terminal Aviation Routine Weather Report) data.
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
     * @param fields optional comma-separated list of fields to include in the response
     * @return ResponseEntity with the latest METAR data or NOT_FOUND if no data exists
     */
    @GetMapping("/{icaoCode}/METAR")
    public ResponseEntity<?> getLatestMetarData(
            @PathVariable String icaoCode,
            @RequestParam(required = false) String fields) {
        logger.info("Retrieving latest METAR data for airport: {}", icaoCode);
        try {
            MetarData metarData = metarService.getLatestMetarData(icaoCode);
            logger.info("Successfully retrieved METAR data for airport: {} with ID: {}", icaoCode, metarData.getId());
            
            // If fields parameter is provided, return partial data
            if (fields != null && !fields.isEmpty()) {
                MetarDataPartial partialData = createPartialMetarData(metarData, fields);
                return new ResponseEntity<>(partialData, HttpStatus.OK);
            }
            
            // Otherwise return full data
            return new ResponseEntity<>(metarData, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("No METAR data found for airport: {}", icaoCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Create a partial METAR data object with only the specified fields.
     *
     * @param metarData the full METAR data object
     * @param fields comma-separated list of fields to include
     * @return a MetarDataPartial object with only the specified fields
     */
    private MetarDataPartial createPartialMetarData(MetarData metarData, String fields) {
        MetarDataPartial partial = new MetarDataPartial();
        Set<String> fieldSet = new HashSet<>(Arrays.asList(fields.split(",")));
        
        if (fieldSet.contains("id")) {
            partial.setId(metarData.getId());
        }
        if (fieldSet.contains("icaoCode")) {
            partial.setIcaoCode(metarData.getIcaoCode());
        }
        if (fieldSet.contains("rawData")) {
            partial.setRawData(metarData.getRawData());
        }
        if (fieldSet.contains("observationTime")) {
            partial.setObservationTime(metarData.getObservationTime());
        }
        if (fieldSet.contains("windDirection")) {
            partial.setWindDirection(metarData.getWindDirection());
        }
        if (fieldSet.contains("windSpeed")) {
            partial.setWindSpeed(metarData.getWindSpeed());
        }
        if (fieldSet.contains("visibility")) {
            partial.setVisibility(metarData.getVisibility());
        }
        if (fieldSet.contains("weatherConditions")) {
            partial.setWeatherConditions(metarData.getWeatherConditions());
        }
        if (fieldSet.contains("temperature")) {
            partial.setTemperature(metarData.getTemperature());
        }
        if (fieldSet.contains("dewPoint")) {
            partial.setDewPoint(metarData.getDewPoint());
        }
        if (fieldSet.contains("altimeter")) {
            partial.setAltimeter(metarData.getAltimeter());
        }
        
        return partial;
    }
    
    /**
     * DTO class for partial METAR data.
     */
    public static class MetarDataPartial {
        private Long id;
        private String icaoCode;
        private String rawData;
        private String observationTime;
        private String windDirection;
        private String windSpeed;
        private String visibility;
        private String weatherConditions;
        private String temperature;
        private String dewPoint;
        private String altimeter;
        
        // Getters and setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getIcaoCode() {
            return icaoCode;
        }
        
        public void setIcaoCode(String icaoCode) {
            this.icaoCode = icaoCode;
        }
        
        public String getRawData() {
            return rawData;
        }
        
        public void setRawData(String rawData) {
            this.rawData = rawData;
        }
        
        public String getObservationTime() {
            return observationTime;
        }
        
        public void setObservationTime(String observationTime) {
            this.observationTime = observationTime;
        }
        
        public String getWindDirection() {
            return windDirection;
        }
        
        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }
        
        public String getWindSpeed() {
            return windSpeed;
        }
        
        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }
        
        public String getVisibility() {
            return visibility;
        }
        
        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }
        
        public String getWeatherConditions() {
            return weatherConditions;
        }
        
        public void setWeatherConditions(String weatherConditions) {
            this.weatherConditions = weatherConditions;
        }
        
        public String getTemperature() {
            return temperature;
        }
        
        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
        
        public String getDewPoint() {
            return dewPoint;
        }
        
        public void setDewPoint(String dewPoint) {
            this.dewPoint = dewPoint;
        }
        
        public String getAltimeter() {
            return altimeter;
        }
        
        public void setAltimeter(String altimeter) {
            this.altimeter = altimeter;
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
