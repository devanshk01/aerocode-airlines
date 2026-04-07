package com.aerocode.airlines.repository;

import com.aerocode.airlines.model.Booking;
import com.aerocode.airlines.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPassenger(Passenger passenger);
    List<Booking> findByPassengerOrderByBookingTimeDesc(Passenger passenger);

    List<Booking> findByFlightId(Long flightId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.flight.id = :flightId AND b.status = 'CONFIRMED'")
    long countConfirmedByFlightId(Long flightId);

    @Query("SELECT b FROM Booking b ORDER BY b.bookingTime DESC")
    List<Booking> findAllOrderByBookingTimeDesc();

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED'")
    long countConfirmed();

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CANCELLED'")
    long countCancelled();

    @Query("SELECT SUM(b.ticketPrice) FROM Booking b WHERE b.status = 'CONFIRMED'")
    Double totalRevenue();
}
