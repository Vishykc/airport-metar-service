package com.viktorvranar.airport_metar_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viktorvranar.airport_metar_service.entity.MetarData;

public interface MetarDataRepository extends JpaRepository<MetarData, Long> {
    
    // Modified to not use observationTime field which is not currently in the entity
    Optional<MetarData> findFirstByIcaoCodeOrderByIdDesc(String icaoCode);

    List<MetarData> findByIcaoCode(String icaoCode);

    // Modified to order by ID instead of observationTime
    @Query("SELECT m FROM MetarData m WHERE m.icaoCode = ?1 ORDER BY m.id DESC")
    List<MetarData> findLatestByIcaoCode(String icaoCode);
}