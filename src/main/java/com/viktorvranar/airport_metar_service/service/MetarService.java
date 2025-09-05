package com.viktorvranar.airport_metar_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.exception.MetarDataNotFoundException;
import com.viktorvranar.airport_metar_service.repository.MetarDataRepository;
    
/**
 * Service class for managing METAR (Meteorological Terminal Aviation Routine Weather Report) data.
 * Provides business logic for storing and retrieving METAR data for airports.
 */
@Service
public class MetarService {
    
    private static final Logger logger = LoggerFactory.getLogger(MetarService.class);
    
    private final MetarDataRepository metarDataRepository;
    
    public MetarService(MetarDataRepository metarDataRepository) {
        this.metarDataRepository = metarDataRepository;
    }
    
    /**
     * Save METAR data for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @param rawData the raw METAR data string
     * @return the saved MetarData entity
     */
    public MetarData saveMetarData(String icaoCode, String rawData) {
        logger.debug("Saving METAR data for airport: {}", icaoCode);
        MetarData metarData = new MetarData();
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        
        // Parse and set METAR elements
        parseAndSetMetarElements(metarData, rawData);
        
        MetarData savedData = metarDataRepository.save(metarData);
        logger.debug("Successfully saved METAR data for airport: {} with ID: {}", icaoCode, savedData.getId());
        return savedData;
    }
    
    /**
     * Parse METAR data and set the individual elements in the MetarData entity.
     *
     * @param metarData the MetarData entity to populate
     * @param rawData the raw METAR data string
     */
    private void parseAndSetMetarElements(MetarData metarData, String rawData) {
        // Remove "METAR" prefix if present
        String data = rawData.startsWith("METAR ") ? rawData.substring(6) : rawData;
        
        // Split the data into tokens
        String[] tokens = data.split("\\s+");
        
        // Parse elements based on METAR format
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            
            // Observation time (e.g., "301200Z")
            if (token.matches("\\d{4,6}Z")) {
                metarData.setObservationTime(token);
            }
            // Wind information (e.g., "12008KT" or "00000KT")
            else if (token.matches("\\d{3,5}(MPS|KT|KMH)")) {
                // Extract wind direction (first 3 digits)
                if (token.length() >= 6) {
                    metarData.setWindDirection(token.substring(0, 3));
                    // Extract wind speed (digits before the unit)
                    String speedPart = token.substring(3);
                    String unit = speedPart.replaceAll("\\d+", "");
                    String speed = speedPart.replace(unit, "");
                    metarData.setWindSpeed(speed);
                }
            }
            // Variable wind direction (e.g., "090V150")
            else if (token.matches("\\d{3}V\\d{3}")) {
                // This is variable wind direction, we could store it in windDirection field
                // or create a separate field for it
            }
            // Visibility (e.g., "9999" or "2000")
            else if (token.matches("\\d{4}|\\d{1,3}SM|M|P\\d{4}")) {
                metarData.setVisibility(token);
            }
            // Runway visual range (e.g., "R04/P2000N")
            else if (token.matches("R\\d{2}[LRC]?/\\S+")) {
                // This is runway visual range, we could store it in weatherConditions field
                // or create a separate field for it
            }
            // Weather conditions (e.g., "NSW", "FEW040", "SCT020")
            else if (token.matches("(NSW|NCD|SKC|CLR|VV\\d{3}|FEW\\d{3}|SCT\\d{3}|BKN\\d{3}|OVC\\d{3}|VV\\d{3}|-\\w{2,4}|\\+\\w{2,4}|\\w{2,4})")) {
                // For simplicity, we'll store all weather conditions in one field
                // In a more complex implementation, we might want separate fields
                String currentConditions = metarData.getWeatherConditions();
                if (currentConditions == null || currentConditions.isEmpty()) {
                    metarData.setWeatherConditions(token);
                } else {
                    metarData.setWeatherConditions(currentConditions + " " + token);
                }
            }
            // Temperature and dew point (e.g., "15/10" or "M01/M05")
            else if (token.matches("[M]?\\d{1,2}/[M]?\\d{1,2}")) {
                String[] tempDew = token.split("/");
                metarData.setTemperature(tempDew[0]);
                metarData.setDewPoint(tempDew[1]);
            }
            // Altimeter/QNH (e.g., "Q1013" or "A2992")
            else if (token.matches("[QA]\\d{4}")) {
                metarData.setAltimeter(token);
            }
        }
    }
    
    /**
     * Get the latest METAR data for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @return the latest MetarData entity
     * @throws MetarDataNotFoundException if no METAR data is found for the airport
     */
    public MetarData getLatestMetarData(String icaoCode) {
        logger.debug("Retrieving latest METAR data for airport: {}", icaoCode);
        Optional<MetarData> metarData = metarDataRepository.findFirstByIcaoCodeOrderByIdDesc(icaoCode);
        if (metarData.isPresent()) {
            logger.debug("Found latest METAR data for airport: {} with ID: {}", icaoCode, metarData.get().getId());
            return metarData.get();
        } else {
            logger.debug("No METAR data found for airport: {}", icaoCode);
            throw new MetarDataNotFoundException(icaoCode);
        }
    }

    /**
     * Get the METAR data history for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @return a list of MetarData entities
     */
    public List<MetarData> getMetarDataHistory(String icaoCode) {
        logger.debug("Retrieving METAR data history for airport: {}", icaoCode);
        List<MetarData> metarDataList = metarDataRepository.findByIcaoCode(icaoCode);
        logger.debug("Found {} METAR data entries for airport: {}", metarDataList.size(), icaoCode);
        return metarDataList;
    }
    
    /**
     * Decode METAR data into natural language.
     *
     * @param metarData the MetarData entity to decode
     * @return a string representation of the METAR data in natural language
     */
    public String decodeMetarData(MetarData metarData) {
        StringBuilder decoded = new StringBuilder();
        
        // Add airport information
        decoded.append("Weather report for airport ").append(metarData.getIcaoCode()).append(". ");
        
        // Add observation time
        if (metarData.getObservationTime() != null && !metarData.getObservationTime().isEmpty()) {
            decoded.append("Observation time: ").append(metarData.getObservationTime()).append(". ");
        }
        
        // Add wind information
        if (metarData.getWindDirection() != null && !metarData.getWindDirection().isEmpty() &&
            metarData.getWindSpeed() != null && !metarData.getWindSpeed().isEmpty()) {
            decoded.append("Wind: from ").append(metarData.getWindDirection())
                   .append(" degrees at ").append(metarData.getWindSpeed()).append(" knots. ");
        }
        
        // Add visibility
        if (metarData.getVisibility() != null && !metarData.getVisibility().isEmpty()) {
            decoded.append("Visibility: ").append(metarData.getVisibility()).append(" meters. ");
        }
        
        // Add weather conditions
        if (metarData.getWeatherConditions() != null && !metarData.getWeatherConditions().isEmpty()) {
            decoded.append("Weather conditions: ").append(decodeWeatherConditions(metarData.getWeatherConditions())).append(". ");
        }
        
        // Add temperature and dew point
        if (metarData.getTemperature() != null && !metarData.getTemperature().isEmpty()) {
            String temperature = metarData.getTemperature();
            if (temperature.startsWith("M")) {
                temperature = "-" + temperature.substring(1);
            }
            decoded.append("Temperature: ").append(temperature).append(" degrees Celsius. ");
        }
        
        if (metarData.getDewPoint() != null && !metarData.getDewPoint().isEmpty()) {
            String dewPoint = metarData.getDewPoint();
            if (dewPoint.startsWith("M")) {
                dewPoint = "-" + dewPoint.substring(1);
            }
            decoded.append("Dew point: ").append(dewPoint).append(" degrees Celsius. ");
        }
        
        // Add altimeter
        if (metarData.getAltimeter() != null && !metarData.getAltimeter().isEmpty()) {
            decoded.append("Altimeter: ").append(metarData.getAltimeter()).append(". ");
        }
        
        return decoded.toString().trim();
    }
    
    /**
     * Decode weather conditions abbreviations into natural language.
     *
     * @param conditions the weather conditions string
     * @return decoded weather conditions
     */
    private String decodeWeatherConditions(String conditions) {
        // Replace common weather condition abbreviations with natural language
        return conditions
            .replace("NSW", "no significant weather")
            .replace("NCD", "nil cloud detected")
            .replace("SKC", "sky clear")
            .replace("CLR", "clear")
            .replace("FEW", "few clouds at ")
            .replace("SCT", "scattered clouds at ")
            .replace("BKN", "broken clouds at ")
            .replace("OVC", "overcast at ")
            .replace("VV", "vertical visibility ")
            .replace("R", "runway ")
            .replace("/", " over ")
            .replace("KT", " knots")
            .replace("MPS", " meters per second")
            .replace("SM", " statute miles")
            .replace("Q", "QNH ")
            .replace("A", "altimeter ");
    }
}
