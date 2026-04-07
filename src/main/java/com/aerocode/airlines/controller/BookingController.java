package com.aerocode.airlines.controller;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.repository.PassengerRepository;
import com.aerocode.airlines.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private FlightService flightService;
    @Autowired private UserService userService;
    @Autowired private PassengerRepository passengerRepository;

    @GetMapping("/book/{flightId}")
    public String bookingForm(@PathVariable Long flightId, Model model, Authentication authentication) {
        Flight flight = flightService.getFlightById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Passenger passenger = passengerRepository.findByUserId(user.getId()).orElse(null);

        model.addAttribute("flight", flight);
        model.addAttribute("ticketPrice", flightService.calculateTicketPrice(flight));
        model.addAttribute("passenger", passenger);
        model.addAttribute("isInternational", flight.getType().equalsIgnoreCase("International"));
        model.addAttribute("isFull", bookingService.isFlightFull(flight));

        return "booking";
    }

    @PostMapping("/book/{flightId}")
    public String processBooking(@PathVariable Long flightId,
                                  @RequestParam(required = false, defaultValue = "0") int passportNum,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(authentication.getName()).orElseThrow();
            Passenger passenger = passengerRepository.findByUserId(user.getId()).orElseThrow();
            Flight flight = flightService.getFlightById(flightId).orElseThrow();

            // Update passport for international flights
            if (flight.getType().equalsIgnoreCase("International")) {
                if (passportNum < 10000000 || passportNum > 99999999) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Please enter a valid 8-digit passport number for international flights.");
                    return "redirect:/book/" + flightId;
                }
                passenger.setPassportNum(passportNum);
                passengerRepository.save(passenger);
            }

            Booking booking = bookingService.bookFlight(passenger, flight);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Ticket booked successfully! Booking #" + String.format("%06d", booking.getId()));
            return "redirect:/my-bookings";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/book/" + flightId;
        }
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(authentication.getName()).orElseThrow();
            Booking cancelled = bookingService.cancelBooking(bookingId, user);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Booking cancelled. Refund of Rs. %.2f will be processed.", cancelled.getRefundAmount()));
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Passenger passenger = passengerRepository.findByUserId(user.getId()).orElse(null);

        if (passenger != null) {
            List<Booking> bookings = bookingService.getBookingsForPassenger(passenger);
            model.addAttribute("bookings", bookings);
        }

        model.addAttribute("flightService", flightService);
        return "my-bookings";
    }

    @GetMapping("/ticket/{bookingId}")
    public String viewTicket(@PathVariable Long bookingId, Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Security: only owner or admin can view
        if (user.getRole() != Role.ADMIN &&
            !booking.getPassenger().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        model.addAttribute("booking", booking);
        return "ticket";
    }
}
