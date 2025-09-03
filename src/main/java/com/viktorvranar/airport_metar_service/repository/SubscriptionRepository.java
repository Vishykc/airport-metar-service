package com.viktorvranar.airport_metar_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viktorvranar.airport_metar_service.entity.Subscription;

/**
 * Repository interface for managing subscription entities.
 * Provides CRUD operations and custom query methods for subscriptions.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Find a subscription by ICAO code.
     *
     * @param icaoCode the ICAO code of the airport
     * @return an Optional containing the subscription if found, or empty if not found
     */
    Optional<Subscription> findByIcaoCode(String icaoCode);

    /**
     * Find all active subscriptions.
     *
     * @return a list of active Subscription entities
     */
    /* List<Subscription> findByActiveTrue(); */

    /**
     * Find subscriptions by active status.
     *
     * @param active the active status to filter by
     * @return a list of Subscription entities with the specified active status
     */
    /* @Query("SELECT s FROM Subscription s WHERE s.active = ?1")
    List<Subscription> findByActiveStatus(boolean active); */

    /**
     * Check if a subscription exists for an ICAO code.
     *
     * @param icaoCode the ICAO code of the airport
     * @return true if a subscription exists, false otherwise
     */
    boolean existsByIcaoCode(String icaoCode);
}