package com.viktorvranar.airport_metar_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viktorvranar.airport_metar_service.entity.MetarData;

/**
 * Repository interface for managing METAR data entities.
 * Provides CRUD operations and custom query methods for METAR data.
 */
public interface MetarDataRepository extends JpaRepository<MetarData, Long> {
    
    /**
     * Find the first METAR data entry for an airport ordered by ID in descending order.
     *
     * @param icaoCode the ICAO code of the airport
     * @return an Optional containing the latest METAR data if found, or empty if not found
     */
    // Modified to not use observationTime field which is not currently in the entity
    Optional<MetarData> findFirstByIcaoCodeOrderByIdDesc(String icaoCode);

    /**
     * Find all METAR data entries for an airport.
     *
     * @param icaoCode the ICAO code of the airport
     * @return a list of METAR data entries for the airport
     */
    List<MetarData> findByIcaoCode(String icaoCode);

    /**
     * Find the latest METAR data entries for an airport ordered by ID in descending order.
     *
     * @param icaoCode the ICAO code of the airport
     * @return a list of METAR data entries for the airport ordered by ID in descending order
     */
    // Modified to order by ID instead of observationTime
    @Query("SELECT m FROM MetarData m WHERE m.icaoCode = ?1 ORDER BY m.id DESC")
    List<MetarData> findLatestByIcaoCode(String icaoCode);
}