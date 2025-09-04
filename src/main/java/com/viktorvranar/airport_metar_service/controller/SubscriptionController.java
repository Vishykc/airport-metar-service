package com.viktorvranar.airport_metar_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.service.SubscriptionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * REST controller for managing airport subscriptions.
 * Provides endpoints for creating, retrieving, and deleting subscriptions.
 */
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    
    /**
     * Subscribe to an airport by ICAO code.
     *
     * @param request the subscription request containing the ICAO code
     * @return ResponseEntity with the created subscription or CONFLICT if already exists
     */
    @PostMapping
    public ResponseEntity<Subscription> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        if (subscriptionService.existsByIcaoCode(request.getIcaoCode())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        Subscription subscription = subscriptionService.createSubscription(request.getIcaoCode());
        return new ResponseEntity<>(subscription, HttpStatus.CREATED);
    }
    
    /**
     * Get all subscriptions.
     *
     * @return ResponseEntity with a list of all subscriptions
     */
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }
       
    /**
     * Unsubscribe from an airport by ICAO code.
     *
     * @param icaoCode the ICAO code of the airport to unsubscribe from
     * @return ResponseEntity with NO_CONTENT if successful or NOT_FOUND if subscription doesn't exist
     */
    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<Void> unsubscribe(@PathVariable String icaoCode) {
        if (!subscriptionService.existsByIcaoCode(icaoCode)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        subscriptionService.deleteSubscription(icaoCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Update the active status of an airport subscription.
     *
     * @param icaoCode the ICAO code of the airport
     * @param request the request containing the new active status
     * @return ResponseEntity with the updated subscription or NOT_FOUND if subscription doesn't exist
     */
    @PutMapping("/{icaoCode}")
    public ResponseEntity<Subscription> updateSubscriptionStatus(
            @PathVariable String icaoCode,
            @Valid @RequestBody SubscriptionStatusRequest request) {
        
        // Convert string to boolean properly handling "0"/"1" and "false"/"true"
        boolean active = parseBoolean(request.getActive());
        
        Subscription updatedSubscription = subscriptionService.updateSubscriptionStatus(icaoCode, active);
        if (updatedSubscription != null) {
            return new ResponseEntity<>(updatedSubscription, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // DTO classes for request bodies
    /**
     * DTO class for subscription request body.
     */
    public static class SubscriptionRequest {
        @NotBlank(message = "ICAO code cannot be blank")
        @Pattern(regexp = "^[A-Z0-9]{4}$", message = "ICAO code must be exactly 4 alphanumeric characters")
        private String icaoCode;
        
        public String getIcaoCode() {
            return icaoCode;
        }
        
        public void setIcaoCode(String icaoCode) {
            this.icaoCode = icaoCode;
        }
    }
    
    /**
     * DTO class for subscription status request body.
     */
    public static class SubscriptionStatusRequest {
        private String active;
        
        public String getActive() {
            return active;
        }
        
        public void setActive(String active) {
            this.active = active;
        }
    }
    
    /**
     * Parse a string to boolean, handling "0"/"1" and "false"/"true" values.
     *
     * @param value the string value to parse
     * @return true if value is "1" or "true" (case-insensitive), false otherwise
     */
    private boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }
        return "1".equals(value) || "true".equalsIgnoreCase(value);
    }
}
