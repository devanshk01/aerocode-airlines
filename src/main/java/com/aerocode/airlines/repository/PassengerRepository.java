package com.aerocode.airlines.repository;

import com.aerocode.airlines.model.Passenger;
import com.aerocode.airlines.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByUser(User user);
    Optional<Passenger> findByMobileNum(long mobileNum);
    Optional<Passenger> findByUserId(Long userId);
    boolean existsByMobileNum(long mobileNum);
}
