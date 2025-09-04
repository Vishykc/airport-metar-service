package com.viktorvranar.airport_metar_service.service;

import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @Test
    void testCreateSubscription() {
        // Given
        String icaoCode = "LDZA";
        Subscription subscription = new Subscription(icaoCode);
        subscription.setId(1L);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // When
        Subscription result = subscriptionService.createSubscription(icaoCode);

        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
        assertTrue(result.isActive());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testFindAllSubscriptions() {
        // Given
        List<Subscription> subscriptions = Arrays.asList(
            new Subscription("LDZA"),
            new Subscription("LDDU")
        );
        
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        // When
        List<Subscription> result = subscriptionService.findAllSubscriptions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void testGetSubscriptionByIcaoCode() {
        // Given
        String icaoCode = "LDZA";
        Subscription subscription = new Subscription(icaoCode);
        subscription.setId(1L);
        
        when(subscriptionRepository.findByIcaoCode(icaoCode)).thenReturn(Optional.of(subscription));

        // When
        Optional<Subscription> result = subscriptionService.getSubscriptionByIcaoCode(icaoCode);

        // Then
        assertTrue(result.isPresent());
        assertEquals(icaoCode, result.get().getIcaoCode());
        verify(subscriptionRepository, times(1)).findByIcaoCode(icaoCode);
    }

    @Test
    void testExistsByIcaoCode() {
        // Given
        String icaoCode = "LDZA";
        when(subscriptionRepository.existsByIcaoCode(icaoCode)).thenReturn(true);

        // When
        boolean result = subscriptionService.existsByIcaoCode(icaoCode);

        // Then
        assertTrue(result);
        verify(subscriptionRepository, times(1)).existsByIcaoCode(icaoCode);
    }

    @Test
    void testDeleteSubscription() {
        // Given
        String icaoCode = "LDZA";
        Subscription subscription = new Subscription(icaoCode);
        subscription.setId(1L);
        
        when(subscriptionRepository.findByIcaoCode(icaoCode)).thenReturn(Optional.of(subscription));

        // When
        subscriptionService.deleteSubscription(icaoCode);

        // Then
        verify(subscriptionRepository, times(1)).delete(subscription);
    }

    @Test
    void testDeleteSubscriptionNotFound() {
        // Given
        String icaoCode = "LDZA";
        when(subscriptionRepository.findByIcaoCode(icaoCode)).thenReturn(Optional.empty());

        // When
        subscriptionService.deleteSubscription(icaoCode);

        // Then
        verify(subscriptionRepository, times(0)).delete(any(Subscription.class));
    }
    
    @Test
    void testUpdateSubscriptionStatus() {
        // Given
        String icaoCode = "LDZA";
        Subscription subscription = new Subscription(icaoCode);
        subscription.setId(1L);
        subscription.setActive(true);
        
        when(subscriptionRepository.findByIcaoCode(icaoCode)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        
        // When
        Subscription result = subscriptionService.updateSubscriptionStatus(icaoCode, false);
        
        // Then
        assertNotNull(result);
        assertEquals(icaoCode, result.getIcaoCode());
        assertFalse(result.isActive());
        verify(subscriptionRepository, times(1)).findByIcaoCode(icaoCode);
        verify(subscriptionRepository, times(1)).save(subscription);
    }
    
    @Test
    void testUpdateSubscriptionStatusNotFound() {
        // Given
        String icaoCode = "LDZA";
        when(subscriptionRepository.findByIcaoCode(icaoCode)).thenReturn(Optional.empty());
        
        // When
        Subscription result = subscriptionService.updateSubscriptionStatus(icaoCode, false);
        
        // Then
        assertNull(result);
        verify(subscriptionRepository, times(1)).findByIcaoCode(icaoCode);
        verify(subscriptionRepository, times(0)).save(any(Subscription.class));
    }
}