package com.viktorvranar.airport_metar_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viktorvranar.airport_metar_service.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByIcaoCode(String icaoCode);

    List<Subscription> findByActiveTrue();

    @Query("SELECT s FROM Subscription s WHERE s.active = ?1")
    List<Subscription> findByActiveStatus(boolean active);

    boolean existsByIcaoCode(String icaoCode);
}