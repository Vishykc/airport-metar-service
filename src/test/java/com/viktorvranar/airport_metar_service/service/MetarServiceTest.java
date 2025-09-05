package com.viktorvranar.airport_metar_service.service;

import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.exception.MetarDataNotFoundException;
import com.viktorvranar.airport_metar_service.repository.MetarDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetarServiceTest {

    @Mock
    private MetarDataRepository metarDataRepository;

    private MetarService metarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        metarService = new MetarService(metarDataRepository);
    }

    @Test
    void testSaveMetarData() {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData savedMetarData = new MetarData();
        savedMetarData.setId(1L);
        savedMetarData.setIcaoCode(icaoCode);
        savedMetarData.setRawData(rawData);
        // Set expected parsed values
        savedMetarData.setObservationTime("030700Z");
        savedMetarData.setWindDirection("000");
        savedMetarData.setWindSpeed("00");
        savedMetarData.setVisibility("9999");
        savedMetarData.setWeatherConditions("NSW SCT040");
        savedMetarData.setTemperature("15");
        savedMetarData.setDewPoint("10");
        savedMetarData.setAltimeter("Q1013");
        
        when(metarDataRepository.save(any(MetarData.class))).thenReturn(savedMetarData);

        // When
        MetarData result = metarService.saveMetarData(icaoCode, rawData);

        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
        assertEquals(rawData, result.getRawData());
        assertEquals("030700Z", result.getObservationTime());
        assertEquals("000", result.getWindDirection());
        assertEquals("00", result.getWindSpeed());
        assertEquals("9999", result.getVisibility());
        assertEquals("NSW SCT040", result.getWeatherConditions());
        assertEquals("15", result.getTemperature());
        assertEquals("10", result.getDewPoint());
        assertEquals("Q1013", result.getAltimeter());
        verify(metarDataRepository, times(1)).save(any(MetarData.class));
    }

    @Test
    void testGetLatestMetarData() {
        // Given
        String icaoCode = "LDZA";
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData("METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG");
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarDataRepository.findFirstByIcaoCodeOrderByIdDesc(icaoCode)).thenReturn(Optional.of(metarData));

        // When
        MetarData result = metarService.getLatestMetarData(icaoCode);

        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
        assertEquals("030700Z", result.getObservationTime());
        assertEquals("000", result.getWindDirection());
        assertEquals("00", result.getWindSpeed());
        assertEquals("9999", result.getVisibility());
        assertEquals("NSW SCT040", result.getWeatherConditions());
        assertEquals("15", result.getTemperature());
        assertEquals("10", result.getDewPoint());
        assertEquals("Q1013", result.getAltimeter());
        verify(metarDataRepository, times(1)).findFirstByIcaoCodeOrderByIdDesc(icaoCode);
    }

    @Test
    void testGetLatestMetarDataNotFound() {
        // Given
        String icaoCode = "LDZA";
        when(metarDataRepository.findFirstByIcaoCodeOrderByIdDesc(icaoCode)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MetarDataNotFoundException.class, () -> {
            metarService.getLatestMetarData(icaoCode);
        });
        verify(metarDataRepository, times(1)).findFirstByIcaoCodeOrderByIdDesc(icaoCode);
    }

    @Test
    void testGetMetarDataHistory() {
        // Given
        String icaoCode = "LDZA";
        List<MetarData> metarDataList = new ArrayList<>();
        MetarData metarData1 = new MetarData();
        metarData1.setId(1L);
        metarData1.setIcaoCode(icaoCode);
        metarData1.setRawData("METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG");
        // Set expected parsed values
        metarData1.setObservationTime("030700Z");
        metarData1.setWindDirection("000");
        metarData1.setWindSpeed("00");
        metarData1.setVisibility("9999");
        metarData1.setWeatherConditions("NSW SCT040");
        metarData1.setTemperature("15");
        metarData1.setDewPoint("10");
        metarData1.setAltimeter("Q1013");
        metarDataList.add(metarData1);
        
        MetarData metarData2 = new MetarData();
        metarData2.setId(2L);
        metarData2.setIcaoCode(icaoCode);
        metarData2.setRawData("METAR LDZA 030800Z 00000KT 9999 NSW SCT040 16/11 Q1014 NOSIG");
        // Set expected parsed values
        metarData2.setObservationTime("030800Z");
        metarData2.setWindDirection("000");
        metarData2.setWindSpeed("00");
        metarData2.setVisibility("9999");
        metarData2.setWeatherConditions("NSW SCT040");
        metarData2.setTemperature("16");
        metarData2.setDewPoint("11");
        metarData2.setAltimeter("Q1014");
        metarDataList.add(metarData2);
        
        when(metarDataRepository.findByIcaoCode(icaoCode)).thenReturn(metarDataList);

        // When
        List<MetarData> result = metarService.getMetarDataHistory(icaoCode);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verify first METAR data
        MetarData firstMetar = result.get(0);
        assertEquals(icaoCode, firstMetar.getIcaoCode());
        assertEquals("030700Z", firstMetar.getObservationTime());
        assertEquals("000", firstMetar.getWindDirection());
        assertEquals("00", firstMetar.getWindSpeed());
        assertEquals("9999", firstMetar.getVisibility());
        assertEquals("NSW SCT040", firstMetar.getWeatherConditions());
        assertEquals("15", firstMetar.getTemperature());
        assertEquals("10", firstMetar.getDewPoint());
        assertEquals("Q1013", firstMetar.getAltimeter());
        // Verify second METAR data
        MetarData secondMetar = result.get(1);
        assertEquals(icaoCode, secondMetar.getIcaoCode());
        assertEquals("030800Z", secondMetar.getObservationTime());
        assertEquals("000", secondMetar.getWindDirection());
        assertEquals("00", secondMetar.getWindSpeed());
        assertEquals("9999", secondMetar.getVisibility());
        assertEquals("NSW SCT040", secondMetar.getWeatherConditions());
        assertEquals("16", secondMetar.getTemperature());
        assertEquals("11", secondMetar.getDewPoint());
        assertEquals("Q1014", secondMetar.getAltimeter());
        verify(metarDataRepository, times(1)).findByIcaoCode(icaoCode);
    }
}