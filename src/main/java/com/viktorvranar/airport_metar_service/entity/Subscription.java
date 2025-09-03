package com.viktorvranar.airport_metar_service.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing an airport subscription.
 * This class maps to the 'subscriptions' table in the database.
 */
@Entity
@Table (name = "subscriptions")
public class Subscription {
    
    /**
     * The unique identifier for the subscription record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ICAO code of the airport for which this subscription is recorded.
     */
    @Column(name = "icao_code", nullable = false, unique = true)
    private String icaoCode;

    /**
     * Indicates whether this subscription is active.
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * Default constructor.
     */
    public Subscription() {}
    
    /**
     * Constructor with ICAO code.
     *
     * @param icaoCode the ICAO code of the airport
     */
    public Subscription(String icaoCode) {
        this.icaoCode = icaoCode;
        this.active = true;
    }
    
    // Getters and Setters
    /**
     * Get the unique identifier for the subscription record.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Set the unique identifier for the subscription record.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Get the ICAO code of the airport for which this subscription is recorded.
     *
     * @return the icaoCode
     */
    public String getIcaoCode() {
        return icaoCode;
    }
    
    /**
     * Set the ICAO code of the airport for which this subscription is recorded.
     *
     * @param icaoCode the icaoCode to set
     */
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
    
    /**
     * Check if this subscription is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set whether this subscription is active.
     *
     * @param active true to activate, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
