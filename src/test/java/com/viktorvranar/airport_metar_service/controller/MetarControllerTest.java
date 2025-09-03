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
                .andExpect(jsonPath("$.rawData").value(rawData));
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
        
        when(metarService.getLatestMetarData(icaoCode)).thenReturn(metarData);

        // When & Then
        mockMvc.perform(get("/airport/{icaoCode}/METAR", icaoCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icaoCode").value(icaoCode))
                .andExpect(jsonPath("$.rawData").value(rawData));
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