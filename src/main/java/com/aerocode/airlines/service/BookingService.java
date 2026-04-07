package com.aerocode.airlines.service;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.repository.AircraftRepository;
import com.aerocode.airlines.repository.BookingRepository;
import com.aerocode.airlines.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private FlightService flightService;

    /**
     * Book a ticket for a passenger on a flight.
     */
    @Transactional
    public Booking bookFlight(Passenger passenger, Flight flight) {
        // Check if flight is full
        if (isFlightFull(flight)) {
            throw new RuntimeException("Flight " + flight.getFlightNumber() + " is fully booked!");
        }

        double price = flightService.calculateTicketPrice(flight);

        // Update passport number if international
        if (flight.getType().equalsIgnoreCase("International") && passenger.getPassportNum() == 0) {
            throw new RuntimeException("Passport number is required for international flights");
        }

        Booking booking = new Booking(passenger, flight, price);
        return bookingRepository.save(booking);
    }

    /**
     * Cancel a booking with 40% cancellation fee (original business logic preserved).
     */
    @Transactional
    public Booking cancelBooking(Long bookingId, User currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Security check: only the booking owner or admin can cancel
        if (currentUser.getRole() != Role.ADMIN &&
            !booking.getPassenger().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        double cancellationFee = booking.getTicketPrice() * 0.4;
        double refund = booking.getTicketPrice() - cancellationFee;

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationTime(LocalDateTime.now());
        booking.setRefundAmount(refund);

        return bookingRepository.save(booking);
    }

    /**
     * Check if flight is full based on confirmed bookings vs aircraft capacity.
     */
    public boolean isFlightFull(Flight flight) {
        long confirmedCount = bookingRepository.countConfirmedByFlightId(flight.getId());
        Optional<Aircraft> aircraftOpt = aircraftRepository.findByNameIgnoreCase(flight.getAircraft());

        if (aircraftOpt.isPresent()) {
            return confirmedCount >= aircraftOpt.get().getPassengerCapacity();
        }
        return false;
    }

    public List<Booking> getBookingsForPassenger(Passenger passenger) {
        return bookingRepository.findByPassengerOrderByBookingTimeDesc(passenger);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllOrderByBookingTimeDesc();
    }

    // Admin dashboard stats
    public long getConfirmedCount() { return bookingRepository.countConfirmed(); }
    public long getCancelledCount() { return bookingRepository.countCancelled(); }
    public long getTotalCount() { return bookingRepository.count(); }

    public double getTotalRevenue() {
        Double revenue = bookingRepository.totalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public long getPassengerCount() {
        return passengerRepository.count();
    }
}
