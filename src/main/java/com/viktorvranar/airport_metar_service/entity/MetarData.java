package com.viktorvranar.airport_metar_service.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "metar")
public class MetarData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icao_code", nullable = false)
    private String icaoCode;

    @Column(name = "raw_data", length = 1000)
    private String rawData;

    @Column(name = "observation_time")
    private LocalDateTime observationTime;

    @Column(name = "wind_speed")
    private Integer windSpeed;
    
    @Column(name = "temperature")
    private Integer temperature;
    
    @Column(name = "visibility")
    private Integer visibility;

    public MetarData() {}
    
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
    
    public LocalDateTime getObservationTime() {
        return observationTime;
    }
    
    public void setObservationTime(LocalDateTime observationTime) {
        this.observationTime = observationTime;
    }
    
    public Integer getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public Integer getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    
    public Integer getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
}
