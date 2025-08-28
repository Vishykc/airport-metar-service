package com.viktorvranar.airport_metar_service.controller;

import java.util.Optional;

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

@RestController
@RequestMapping("/airport")
public class MetarController {
    
    @Autowired
    private MetarService metarService;
    
    // Store METAR data for an airport
    @PostMapping("/{icaoCode}/METAR")
    public ResponseEntity<MetarData> storeMetarData(
            @PathVariable String icaoCode,
            @RequestBody MetarDataRequest request) {
        
        MetarData metarData = metarService.saveMetarData(icaoCode, request.getData());
        return new ResponseEntity<>(metarData, HttpStatus.CREATED);
    }
    
    // Retrieve the latest METAR data for an airport
    @GetMapping("/{icaoCode}/METAR")
    public ResponseEntity<MetarData> getLatestMetarData(@PathVariable String icaoCode) {
        Optional<MetarData> metarDataOpt = metarService.getLatestMetarData(icaoCode);
        
        if (!metarDataOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(metarDataOpt.get(), HttpStatus.OK);
    }
    
    // DTO class for request body
    public static class MetarDataRequest {
        private String data;
        
        public String getData() {
            return data;
        }
        
        public void setData(String data) {
            this.data = data;
        }
    }
}
