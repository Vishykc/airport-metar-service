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
        
        when(metarDataRepository.save(any(MetarData.class))).thenReturn(savedMetarData);

        // When
        MetarData result = metarService.saveMetarData(icaoCode, rawData);

        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
        assertEquals(rawData, result.getRawData());
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
        
        when(metarDataRepository.findFirstByIcaoCodeOrderByIdDesc(icaoCode)).thenReturn(Optional.of(metarData));

        // When
        MetarData result = metarService.getLatestMetarData(icaoCode);

        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
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
        metarDataList.add(metarData1);
        
        MetarData metarData2 = new MetarData();
        metarData2.setId(2L);
        metarData2.setIcaoCode(icaoCode);
        metarData2.setRawData("METAR LDZA 030800Z 00000KT 9999 NSW SCT040 16/11 Q1014 NOSIG");
        metarDataList.add(metarData2);
        
        when(metarDataRepository.findByIcaoCode(icaoCode)).thenReturn(metarDataList);

        // When
        List<MetarData> result = metarService.getMetarDataHistory(icaoCode);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(metarDataRepository, times(1)).findByIcaoCode(icaoCode);
    }
}