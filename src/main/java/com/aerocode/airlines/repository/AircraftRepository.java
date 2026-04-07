package com.aerocode.airlines.repository;

import com.aerocode.airlines.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
