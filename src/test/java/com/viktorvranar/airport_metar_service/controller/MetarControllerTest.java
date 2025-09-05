package com.viktorvranar.airport_metar_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktorvranar.airport_metar_service.entity.MetarData;
import com.viktorvranar.airport_metar_service.exception.MetarDataNotFoundException;
import com.viktorvranar.airport_metar_service.service.MetarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetarController.class)
class MetarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetarService metarService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testStoreMetarData() throws Exception {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarService.saveMetarData(anyString(), anyString())).thenReturn(metarData);

        // Create request object
        MetarController.MetarDataRequest request = new MetarController.MetarDataRequest();
        request.setData(rawData);

        // When & Then
        mockMvc.perform(post("/airport/{icaoCode}/METAR", icaoCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icaoCode").value(icaoCode))
                .andExpect(jsonPath("$.rawData").value(rawData))
                .andExpect(jsonPath("$.observationTime").value("030700Z"))
                .andExpect(jsonPath("$.windDirection").value("000"))
                .andExpect(jsonPath("$.windSpeed").value("00"))
                .andExpect(jsonPath("$.visibility").value("9999"))
                .andExpect(jsonPath("$.weatherConditions").value("NSW SCT040"))
                .andExpect(jsonPath("$.temperature").value("15"))
                .andExpect(jsonPath("$.dewPoint").value("10"))
                .andExpect(jsonPath("$.altimeter").value("Q1013"));
    }

    @Test
    void testGetLatestMetarData() throws Exception {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarService.getLatestMetarData(icaoCode)).thenReturn(metarData);

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icaoCode").value(icaoCode))
                .andExpect(jsonPath("$.rawData").value(rawData))
                .andExpect(jsonPath("$.observationTime").value("030700Z"))
                .andExpect(jsonPath("$.windDirection").value("000"))
                .andExpect(jsonPath("$.windSpeed").value("00"))
                .andExpect(jsonPath("$.visibility").value("9999"))
                .andExpect(jsonPath("$.weatherConditions").value("NSW SCT040"))
                .andExpect(jsonPath("$.temperature").value("15"))
                .andExpect(jsonPath("$.dewPoint").value("10"))
                .andExpect(jsonPath("$.altimeter").value("Q1013"));
    }
    
    @Test
    void testGetLatestMetarDataWithFields() throws Exception {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarService.getLatestMetarData(icaoCode)).thenReturn(metarData);

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode)
                .param("fields", "windSpeed,temperature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.icaoCode").doesNotExist())
                .andExpect(jsonPath("$.rawData").doesNotExist())
                .andExpect(jsonPath("$.observationTime").doesNotExist())
                .andExpect(jsonPath("$.windDirection").doesNotExist())
                .andExpect(jsonPath("$.windSpeed").value("00"))
                .andExpect(jsonPath("$.visibility").doesNotExist())
                .andExpect(jsonPath("$.weatherConditions").doesNotExist())
                .andExpect(jsonPath("$.temperature").value("15"))
                .andExpect(jsonPath("$.dewPoint").doesNotExist())
                .andExpect(jsonPath("$.altimeter").doesNotExist());
    }
    
    @Test
    void testGetLatestMetarDataWithAllFields() throws Exception {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarService.getLatestMetarData(icaoCode)).thenReturn(metarData);

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode)
                .param("fields", "id,icaoCode,rawData,observationTime,windDirection,windSpeed,visibility,weatherConditions,temperature,dewPoint,altimeter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icaoCode").value(icaoCode))
                .andExpect(jsonPath("$.rawData").value(rawData))
                .andExpect(jsonPath("$.observationTime").value("030700Z"))
                .andExpect(jsonPath("$.windDirection").value("000"))
                .andExpect(jsonPath("$.windSpeed").value("00"))
                .andExpect(jsonPath("$.visibility").value("9999"))
                .andExpect(jsonPath("$.weatherConditions").value("NSW SCT040"))
                .andExpect(jsonPath("$.temperature").value("15"))
                .andExpect(jsonPath("$.dewPoint").value("10"))
                .andExpect(jsonPath("$.altimeter").value("Q1013"));
    }
    
    @Test
    void testGetLatestMetarDataDecoded() throws Exception {
        // Given
        String icaoCode = "LDZA";
        String rawData = "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG";
        
        MetarData metarData = new MetarData();
        metarData.setId(1L);
        metarData.setIcaoCode(icaoCode);
        metarData.setRawData(rawData);
        // Set expected parsed values
        metarData.setObservationTime("030700Z");
        metarData.setWindDirection("000");
        metarData.setWindSpeed("00");
        metarData.setVisibility("9999");
        metarData.setWeatherConditions("NSW SCT040");
        metarData.setTemperature("15");
        metarData.setDewPoint("10");
        metarData.setAltimeter("Q1013");
        
        when(metarService.getLatestMetarData(icaoCode)).thenReturn(metarData);
        when(metarService.decodeMetarData(metarData)).thenReturn("Weather report for airport LDZA. Observation time: 030700Z. Wind: from 000 degrees at 00 knots. Visibility: 9999 meters. Weather conditions: no significant weather scattered clouds at 040. Temperature: 15 degrees Celsius. Dew point: 10 degrees Celsius. Altimeter: Q1013.");

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode)
                .param("decoded", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decodedData").value("Weather report for airport LDZA. Observation time: 030700Z. Wind: from 000 degrees at 00 knots. Visibility: 9999 meters. Weather conditions: no significant weather scattered clouds at 040. Temperature: 15 degrees Celsius. Dew point: 10 degrees Celsius. Altimeter: Q1013."));
    }

    @Test
    void testGetLatestMetarDataNotFound() throws Exception {
        // Given
        String icaoCode = "LDZA";
        when(metarService.getLatestMetarData(icaoCode)).thenThrow(new MetarDataNotFoundException(icaoCode));

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testStoreMetarDataValidationFailure() throws Exception {
        // Given
        String icaoCode = "LDZA";
        
        // Create request object with blank data
        MetarController.MetarDataRequest request = new MetarController.MetarDataRequest();
        request.setData(""); // Blank data should trigger validation error

        // When & Then
        mockMvc.perform(post("/airport/{icaoCode}/METAR", icaoCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}