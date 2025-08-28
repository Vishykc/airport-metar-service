package com.viktorvranar.airport_metar_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(String icaoCode) {
        Subscription subscription = new Subscription(icaoCode);
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    /* TODO this is for extra tasks
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findByActiveTrue();
    } */
    
    public Optional<Subscription> getSubscriptionByIcaoCode(String icaoCode) {
        return subscriptionRepository.findByIcaoCode(icaoCode);
    }
    
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

    public void deleteSubscription(String icaoCode) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByIcaoCode(icaoCode);
        if (subscriptionOpt.isPresent()) {
            subscriptionRepository.delete(subscriptionOpt.get());
        }
    }

    // DTO classes for request bodies
    public static class SubscriptionRequest {
        private String icaoCode;
        
        public String getIcaoCode() {
            return icaoCode;
        }
        
        public void setIcaoCode(String icaoCode) {
            this.icaoCode = icaoCode;
        }
    }
    
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
