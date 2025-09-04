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
        
        MetarData savedData = metarDataRepository.save(metarData);
        logger.debug("Successfully saved METAR data for airport: {} with ID: {}", icaoCode, savedData.getId());
        return savedData;
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
}
