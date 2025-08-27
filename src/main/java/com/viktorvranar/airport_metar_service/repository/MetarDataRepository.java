package com.viktorvranar.airport_metar_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viktorvranar.airport_metar_service.entity.MetarData;

public interface MetarDataRepository extends JpaRepository<MetarData, Long> {
    
    Optional<MetarData> findFirstByIcaoCodeOrderByObservationTimeDesc(String icaoCode);

    List<MetarData> findByIcaoCode(String icaoCode);

    @Query("SELECT m FROM MetarData m WHERE m.icaoCode = ?1 ORDER BY m.observationTime DESC")
    List<MetarData> findLatestByIcaoCode(String icaoCode);
}