package com.aerocode.airlines.controller;

import com.aerocode.airlines.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @GetMapping("/aircraft")
    public String aircraftFleet(Model model) {
        model.addAttribute("aircraftList", aircraftService.getAllAircraft());
        return "aircraft";
    }
}
