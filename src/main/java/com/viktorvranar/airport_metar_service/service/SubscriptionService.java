package com.viktorvranar.airport_metar_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.repository.SubscriptionRepository;

@Service
/**
 * Service class for managing airport subscriptions.
 * Provides business logic for creating, retrieving, and deleting subscriptions.
 */
public class SubscriptionService {
    
    private final SubscriptionRepository subscriptionRepository;
    
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Create a new subscription for an airport.
     *
     * @param icaoCode the ICAO code of the airport to subscribe to
     * @return the created Subscription entity
     */
    public Subscription createSubscription(String icaoCode) {
        Subscription subscription = new Subscription(icaoCode);
        return subscriptionRepository.save(subscription);
    }

    /**
     * Find all subscriptions.
     *
     * @return a list of all Subscription entities
     */
    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    /* TODO this is for extra tasks
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findByActiveTrue();
    } */
    
    /**
     * Get a subscription by ICAO code.
     *
     * @param icaoCode the ICAO code of the airport
     * @return an Optional containing the Subscription entity if found, or empty if not found
     */
    public Optional<Subscription> getSubscriptionByIcaoCode(String icaoCode) {
        return subscriptionRepository.findByIcaoCode(icaoCode);
    }
    
    /**
     * Check if a subscription exists for an ICAO code.
     *
     * @param icaoCode the ICAO code of the airport
     * @return true if a subscription exists, false otherwise
     */
    public boolean existsByIcaoCode(String icaoCode) {
        return subscriptionRepository.existsByIcaoCode(icaoCode);
    }

    /* TODO for extra tasks
    public Subscription updateSubscriptionStatus(String icaoCode, boolean active) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByIcaoCode(icaoCode);
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.setActive(active);
            return subscriptionRepository.save(subscription);
        }
        return null;
    } */

    /**
     * Delete a subscription by ICAO code.
     *
     * @param icaoCode the ICAO code of the airport to unsubscribe from
     */
    public void deleteSubscription(String icaoCode) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByIcaoCode(icaoCode);
        if (subscriptionOpt.isPresent()) {
            subscriptionRepository.delete(subscriptionOpt.get());
        }
    }

    // DTO classes for request bodies
    /**
     * DTO class for subscription request body.
     */
    public static class SubscriptionRequest {
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
    
}
