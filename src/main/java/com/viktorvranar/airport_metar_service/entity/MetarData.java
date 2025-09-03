package com.viktorvranar.airport_metar_service.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing METAR (Meteorological Terminal Aviation Routine Weather Report) data.
 * This class maps to the 'metar' table in the database.
 */
@Entity
@Table(name = "metar")
public class MetarData {

    /**
     * The unique identifier for the METAR data record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ICAO code of the airport for which this METAR data is recorded.
     */
    @Column(name = "icao_code", nullable = false)
    private String icaoCode;

    /**
     * The raw METAR data string.
     */
    @Column(name = "raw_data", length = 1000)
    private String rawData;

    /**
     * The observation time of the METAR data.
     */
    /* @Column(name = "observation_time")
    private LocalDateTime observationTime; */

    /**
     * The wind speed in knots.
     */
    /*@Column(name = "wind_speed")
    private Integer windSpeed; */
    
    /**
     * The temperature in Celsius.
     */
    /*@Column(name = "temperature")
    private Integer temperature; */
    
    /**
     * The visibility in meters.
     */
   /*  @Column(name = "visibility")
    private Integer visibility;
    public MetarData() {} */
    
    /**
     * Get the unique identifier for the METAR data record.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Set the unique identifier for the METAR data record.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Get the ICAO code of the airport for which this METAR data is recorded.
     *
     * @return the icaoCode
     */
    public String getIcaoCode() {
        return icaoCode;
    }
    
    /**
     * Set the ICAO code of the airport for which this METAR data is recorded.
     *
     * @param icaoCode the icaoCode to set
     */
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
    
    /**
     * Get the raw METAR data string as received from the weather station.
     *
     * @return the rawData
     */
    public String getRawData() {
        return rawData;
    }
    
    /**
     * Set the raw METAR data string as received from the weather station.
     *
     * @param rawData the rawData to set
     */
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
    
    /**
     * Get the observation time of the METAR data.
     *
     * @return the observationTime
     */
    /* public LocalDateTime getObservationTime() {
        return observationTime;
    } */
    
    /**
     * Set the observation time of the METAR data.
     *
     * @param observationTime the observationTime to set
     */
    /* public void setObservationTime(LocalDateTime observationTime) {
        this.observationTime = observationTime;
    } */
    
    /**
     * Get the wind speed in knots.
     *
     * @return the windSpeed
     */
    /* public Integer getWindSpeed() {
        return windSpeed;
    } */
    
    /**
     * Set the wind speed in knots.
     *
     * @param windSpeed the windSpeed to set
     */
    /* public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    } */
    
    /**
     * Get the temperature in Celsius.
     *
     * @return the temperature
     */
    /* public Integer getTemperature() {
        return temperature;
    } */
    
    /**
     * Set the temperature in Celsius.
     *
     * @param temperature the temperature to set
     */
    /* public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    } */
    
    /**
     * Get the visibility in meters.
     *
     * @return the visibility
     */
    /* public Integer getVisibility() {
        return visibility;
    } */
    
    /**
     * Set the visibility in meters.
     *
     * @param visibility the visibility to set
     */
    /* public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    } */
}
