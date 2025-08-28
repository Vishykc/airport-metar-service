package com.viktorvranar.airport_metar_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    
    @Autowired
    private SubscriptionService subscriptionService;
    
    // Subscribe to an airport
    @PostMapping
    public ResponseEntity<Subscription> subscribe(@RequestBody SubscriptionRequest request) {
        if (subscriptionService.existsByIcaoCode(request.getIcaoCode())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        Subscription subscription = subscriptionService.createSubscription(request.getIcaoCode());
        return new ResponseEntity<>(subscription, HttpStatus.CREATED);
    }
    
    // Get all subscriptions
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }
       
    // Unsubscribe from an airport
    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<Void> unsubscribe(@PathVariable String icaoCode) {
        if (!subscriptionService.existsByIcaoCode(icaoCode)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        subscriptionService.deleteSubscription(icaoCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
