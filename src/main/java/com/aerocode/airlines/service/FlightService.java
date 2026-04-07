package com.aerocode.airlines.service;

import com.aerocode.airlines.model.Aircraft;
import com.aerocode.airlines.model.Flight;
import com.aerocode.airlines.repository.AircraftRepository;
import com.aerocode.airlines.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Optional<Flight> getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    public List<Flight> searchFlights(String fromCity, String toCity, String date, String type) {
        String from = (fromCity != null && !fromCity.isBlank()) ? fromCity : null;
        String to = (toCity != null && !toCity.isBlank()) ? toCity : null;
        String d = (date != null && !date.isBlank()) ? date : null;
        String t = (type != null && !type.isBlank()) ? type : null;

        if (from == null && to == null && d == null && t == null) {
            return flightRepository.findAll();
        }

        return flightRepository.searchFlights(from, to, d, t);
    }

    public List<String> getAllDepartureCities() {
        return flightRepository.findAllDepartureCities();
    }

    public List<String> getAllArrivalCities() {
        return flightRepository.findAllArrivalCities();
    }

    /**
     * Calculates ticket price using the original AeroCode formula:
     * price = (FUEL_PRICE × fuelConsumption × distance) / (passengerCapacity + crewCapacity)
     */
    public double calculateTicketPrice(Flight flight) {
        final double PRICE_OF_ONE_LITRE_FUEL = 1120.0; // Rs. 1120 per Litre

        Optional<Aircraft> aircraftOpt = aircraftRepository.findByNameIgnoreCase(flight.getAircraft());
        if (aircraftOpt.isPresent()) {
            Aircraft aircraft = aircraftOpt.get();
            return (PRICE_OF_ONE_LITRE_FUEL * aircraft.getFuelConsumption() * flight.getDistance())
                    / (aircraft.getPassengerCapacity() + aircraft.getCrewCapacity());
        }
        return 0;
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    public long getFlightCount() {
        return flightRepository.count();
    }
}
