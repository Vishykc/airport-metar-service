package com.viktorvranar.airport_metar_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktorvranar.airport_metar_service.entity.Subscription;
import com.viktorvranar.airport_metar_service.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSubscribe() throws Exception {
        // Given
        String icaoCode = "LDZA";
        
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setIcaoCode(icaoCode);
        
        when(subscriptionService.existsByIcaoCode(icaoCode)).thenReturn(false);
        when(subscriptionService.createSubscription(icaoCode)).thenReturn(subscription);

        // Create request object
        SubscriptionController.SubscriptionRequest request = new SubscriptionController.SubscriptionRequest();
        request.setIcaoCode(icaoCode);

        // When & Then
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icaoCode").value(icaoCode));
    }
    
    @Test
    void testSubscribeValidationFailure() throws Exception {
        // Given
        // Create request object with invalid ICAO code
        SubscriptionController.SubscriptionRequest request = new SubscriptionController.SubscriptionRequest();
        request.setIcaoCode("INVALID"); // Invalid ICAO code should trigger validation error

        // When & Then
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testSubscribeConflict() throws Exception {
        // Given
        String icaoCode = "LDZA";
        
        when(subscriptionService.existsByIcaoCode(icaoCode)).thenReturn(true);

        // Create request object
        SubscriptionController.SubscriptionRequest request = new SubscriptionController.SubscriptionRequest();
        request.setIcaoCode(icaoCode);

        // When & Then
        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetAllSubscriptions() throws Exception {
        // Given
        Subscription subscription1 = new Subscription("LDZA");
        subscription1.setId(1L);
        Subscription subscription2 = new Subscription("LDDU");
        subscription2.setId(2L);
        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);
        
        when(subscriptionService.findAllSubscriptions()).thenReturn(subscriptions);

        // When & Then
        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].icaoCode").value("LDZA"))
                .andExpect(jsonPath("$[1].icaoCode").value("LDDU"));
    }

    @Test
    void testUnsubscribe() throws Exception {
        // Given
        String icaoCode = "LDZA";
        
        when(subscriptionService.existsByIcaoCode(icaoCode)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/subscriptions/{icaoCode}", icaoCode))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testUnsubscribeNotFound() throws Exception {
        // Given
        String icaoCode = "LDZA";
        
        when(subscriptionService.existsByIcaoCode(icaoCode)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/subscriptions/{icaoCode}", icaoCode))
                .andExpect(status().isNotFound());
    }
}