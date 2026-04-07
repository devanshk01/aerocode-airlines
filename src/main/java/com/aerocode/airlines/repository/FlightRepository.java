package com.aerocode.airlines.repository;

import com.aerocode.airlines.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByFromCityIgnoreCase(String fromCity);
    List<Flight> findByToCityIgnoreCase(String toCity);
    List<Flight> findByDate(String date);
    List<Flight> findByTypeIgnoreCase(String type);

    @Query("SELECT f FROM Flight f WHERE " +
           "(:fromCity IS NULL OR LOWER(f.fromCity) = LOWER(:fromCity)) AND " +
           "(:toCity IS NULL OR LOWER(f.toCity) = LOWER(:toCity)) AND " +
           "(:date IS NULL OR f.date = :date) AND " +
           "(:type IS NULL OR LOWER(f.type) = LOWER(:type))")
    List<Flight> searchFlights(@Param("fromCity") String fromCity,
                               @Param("toCity") String toCity,
                               @Param("date") String date,
                               @Param("type") String type);

    @Query("SELECT DISTINCT f.fromCity FROM Flight f ORDER BY f.fromCity")
    List<String> findAllDepartureCities();

    @Query("SELECT DISTINCT f.toCity FROM Flight f ORDER BY f.toCity")
    List<String> findAllArrivalCities();
}
