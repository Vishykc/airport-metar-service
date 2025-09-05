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
     * The timestamp of the METAR observation.
     */
    @Column(name = "observation_time")
    private String observationTime;
    
    /**
     * The wind direction in degrees.
     */
    @Column(name = "wind_direction")
    private String windDirection;
    
    /**
     * The wind speed.
     */
    @Column(name = "wind_speed")
    private String windSpeed;
    
    /**
     * The visibility in meters.
     */
    @Column(name = "visibility")
    private String visibility;
    
    /**
     * The weather conditions.
     */
    @Column(name = "weather_conditions")
    private String weatherConditions;
    
    /**
     * The temperature in Celsius.
     */
    @Column(name = "temperature")
    private String temperature;
    
    /**
     * The dew point in Celsius.
     */
    @Column(name = "dew_point")
    private String dewPoint;
    
    /**
     * The altimeter setting (QNH) in hPa.
     */
    @Column(name = "altimeter")
    private String altimeter;
    
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
     * Get the timestamp of the METAR observation.
     *
     * @return the observationTime
     */
    public String getObservationTime() {
        return observationTime;
    }
    
    /**
     * Set the timestamp of the METAR observation.
     *
     * @param observationTime the observationTime to set
     */
    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }
    
    /**
     * Get the wind direction in degrees.
     *
     * @return the windDirection
     */
    public String getWindDirection() {
        return windDirection;
    }
    
    /**
     * Set the wind direction in degrees.
     *
     * @param windDirection the windDirection to set
     */
    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
    
    /**
     * Get the wind speed.
     *
     * @return the windSpeed
     */
    public String getWindSpeed() {
        return windSpeed;
    }
    
    /**
     * Set the wind speed.
     *
     * @param windSpeed the windSpeed to set
     */
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    /**
     * Get the visibility in meters.
     *
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }
    
    /**
     * Set the visibility in meters.
     *
     * @param visibility the visibility to set
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    
    /**
     * Get the weather conditions.
     *
     * @return the weatherConditions
     */
    public String getWeatherConditions() {
        return weatherConditions;
    }
    
    /**
     * Set the weather conditions.
     *
     * @param weatherConditions the weatherConditions to set
     */
    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }
    
    /**
     * Get the temperature in Celsius.
     *
     * @return the temperature
     */
    public String getTemperature() {
        return temperature;
    }
    
    /**
     * Set the temperature in Celsius.
     *
     * @param temperature the temperature to set
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    /**
     * Get the dew point in Celsius.
     *
     * @return the dewPoint
     */
    public String getDewPoint() {
        return dewPoint;
    }
    
    /**
     * Set the dew point in Celsius.
     *
     * @param dewPoint the dewPoint to set
     */
    public void setDewPoint(String dewPoint) {
        this.dewPoint = dewPoint;
    }
    
    /**
     * Get the altimeter setting (QNH) in hPa.
     *
     * @return the altimeter
     */
    public String getAltimeter() {
        return altimeter;
    }
    
    /**
     * Set the altimeter setting (QNH) in hPa.
     *
     * @param altimeter the altimeter to set
     */
    public void setAltimeter(String altimeter) {
        this.altimeter = altimeter;
    }
}
