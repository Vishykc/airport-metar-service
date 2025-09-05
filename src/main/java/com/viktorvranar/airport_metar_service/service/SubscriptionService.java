package com.viktorvranar.airport_metar_service.service;

import java.util.List;
import java.util.Optional;

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

    /**
     * Find all active subscriptions.
     *
     * @return a list of active Subscription entities
     */
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findByActiveTrue();
    }
    
    /**
     * Find subscriptions by active status.
     *
     * @param active the active status to filter by
     * @return a list of Subscription entities with the specified active status
     */
    public List<Subscription> getSubscriptionsByActiveStatus(boolean active) {
        return subscriptionRepository.findByActiveStatus(active);
    }
    
    /**
     * Find subscriptions by ICAO code pattern (case-insensitive).
     *
     * @param icaoCodePattern the pattern to match ICAO codes against (use % for wildcards)
     * @return a list of Subscription entities matching the pattern
     */
    public List<Subscription> getSubscriptionsByIcaoCodePattern(String icaoCodePattern) {
        return subscriptionRepository.findByIcaoCodePattern(icaoCodePattern);
    }
    
    /**
     * Find subscriptions by active status and ICAO code pattern (case-insensitive).
     *
     * @param active the active status to filter by
     * @param icaoCodePattern the pattern to match ICAO codes against (use % for wildcards)
     * @return a list of Subscription entities matching the criteria
     */
    public List<Subscription> getSubscriptionsByActiveStatusAndIcaoCodePattern(boolean active, String icaoCodePattern) {
        return subscriptionRepository.findByActiveStatusAndIcaoCodePattern(active, icaoCodePattern);
    }
    
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
    
    /**
     * Update the active status of a subscription.
     *
     * @param icaoCode the ICAO code of the airport
     * @param active the new active status (true to activate, false to deactivate)
     * @return the updated Subscription entity, or null if not found
     */
    public Subscription updateSubscriptionStatus(String icaoCode, boolean active) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByIcaoCode(icaoCode);
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.setActive(active);
            return subscriptionRepository.save(subscription);
        }
        return null;
    }
    
    
}
