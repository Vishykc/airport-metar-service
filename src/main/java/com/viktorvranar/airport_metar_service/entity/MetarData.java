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
}
