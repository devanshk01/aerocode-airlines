package com.aerocode.airlines.controller;

import com.aerocode.airlines.model.Flight;
import com.aerocode.airlines.model.User;
import com.aerocode.airlines.service.BookingService;
import com.aerocode.airlines.service.FlightService;
import com.aerocode.airlines.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        // Stats for the landing page
        model.addAttribute("flightCount", flightService.getFlightCount());
        model.addAttribute("destinationCount", flightService.getAllArrivalCities().size());

        // Featured flights (first 6)
        List<Flight> allFlights = flightService.getAllFlights();
        List<Flight> featured = allFlights.stream().limit(6).toList();
        model.addAttribute("featuredFlights", featured);

        // Departure cities for search
        model.addAttribute("departureCities", flightService.getAllDepartureCities());
        model.addAttribute("arrivalCities", flightService.getAllArrivalCities());

        // Pass flightService for price calculations in template
        model.addAttribute("flightService", flightService);

        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            model.addAttribute("currentUser", user);
        }

        return "index";
    }
}
