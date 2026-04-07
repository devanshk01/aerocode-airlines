package com.aerocode.airlines.controller;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.repository.PassengerRepository;
import com.aerocode.airlines.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private BookingService bookingService;
    @Autowired private FlightService flightService;
    @Autowired private UserService userService;
    @Autowired private AircraftService aircraftService;
    @Autowired private PassengerRepository passengerRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBookings", bookingService.getTotalCount());
        model.addAttribute("confirmedBookings", bookingService.getConfirmedCount());
        model.addAttribute("cancelledBookings", bookingService.getCancelledCount());
        model.addAttribute("totalRevenue", bookingService.getTotalRevenue());
        model.addAttribute("totalFlights", flightService.getFlightCount());
        model.addAttribute("totalPassengers", bookingService.getPassengerCount());
        model.addAttribute("totalAircraft", aircraftService.getAllAircraft().size());

        // Recent bookings
        List<Booking> recentBookings = bookingService.getAllBookings();
        model.addAttribute("recentBookings", recentBookings.stream().limit(10).toList());

        return "admin/dashboard";
    }

    @GetMapping("/passengers")
    public String allPassengers(Model model) {
        List<Passenger> passengers = passengerRepository.findAll();
        model.addAttribute("passengers", passengers);
        return "admin/passengers";
    }

    @GetMapping("/flights")
    public String manageFlights(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
        model.addAttribute("flightService", flightService);
        return "admin/flights-manage";
    }

    @GetMapping("/flights/add")
    public String addFlightForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("aircraft", aircraftService.getAllAircraft());
        return "admin/flight-form";
    }

    @PostMapping("/flights/add")
    public String addFlight(@ModelAttribute Flight flight, RedirectAttributes redirectAttributes) {
        try {
            flightService.saveFlight(flight);
            redirectAttributes.addFlashAttribute("successMessage", "Flight added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding flight: " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }

    @PostMapping("/flights/delete/{id}")
    public String deleteFlight(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            flightService.deleteFlight(id);
            redirectAttributes.addFlashAttribute("successMessage", "Flight deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }

    @GetMapping("/bookings")
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }
}
