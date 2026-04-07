package com.aerocode.airlines.controller;

import com.aerocode.airlines.model.Flight;
import com.aerocode.airlines.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public String flightsPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String type,
            Model model) {

        List<Flight> flights;
        boolean hasSearch = (from != null && !from.isBlank()) ||
                           (to != null && !to.isBlank()) ||
                           (date != null && !date.isBlank()) ||
                           (type != null && !type.isBlank());

        if (hasSearch) {
            flights = flightService.searchFlights(from, to, date, type);
            model.addAttribute("searched", true);
        } else {
            flights = flightService.getAllFlights();
            model.addAttribute("searched", false);
        }

        // Calculate prices for each flight
        for (Flight f : flights) {
            double price = flightService.calculateTicketPrice(f);
            // Store price in a transient way (using a map in model)
            model.addAttribute("price_" + f.getId(), price);
        }

        model.addAttribute("flights", flights);
        model.addAttribute("departureCities", flightService.getAllDepartureCities());
        model.addAttribute("arrivalCities", flightService.getAllArrivalCities());
        model.addAttribute("searchFrom", from);
        model.addAttribute("searchTo", to);
        model.addAttribute("searchDate", date);
        model.addAttribute("searchType", type);
        model.addAttribute("flightService", flightService);

        return "flights";
    }

    @GetMapping("/{id}")
    public String flightDetail(@PathVariable Long id, Model model) {
        Flight flight = flightService.getFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        model.addAttribute("flight", flight);
        model.addAttribute("ticketPrice", flightService.calculateTicketPrice(flight));

        return "flight-detail";
    }
}
