package com.viktorvranar.airport_metar_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.repository.MetarDataRepository;
    
@Service
public class MetarService {
    
    @Autowired
    private MetarDataRepository metarDataRepository;
    
    public MetarData saveMetarData(String icaoCode, String rawData) {
        MetarData metarData = new MetarData();
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        /* TODO for extra tasks
        metarData.setObservationTime(LocalDateTime.now()); */
        
        /* TODO for EKSTRA TASKS Parse METAR data for specific fields
        parseMetarData(metarData, rawData); */
        
        return metarDataRepository.save(metarData);
    }
    
    public Optional<MetarData> getLatestMetarData(String icaoCode) {
        return metarDataRepository.findFirstByIcaoCodeOrderByObservationTimeDesc(icaoCode);
    }
    
    public List<MetarData> getMetarDataHistory(String icaoCode) {
        return metarDataRepository.findByIcaoCode(icaoCode);
    }
}
    
        /* TODO for EKSTRA TASKS Parse METAR data for specific fields
        private void parseMetarData(MetarData metarData, String rawData) {

    } */
