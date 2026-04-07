package com.aerocode.airlines.config;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Seeds the database on first startup:
 * - 6 aircraft from original AllAircrafts class
 * - 101 flights from airline_data.csv
 * - 8 passengers from passenger_data.csv (linked to auto-created users)
 * - 1 default admin account
 *
 * Only runs if the database is empty (idempotent).
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private AircraftRepository aircraftRepository;
    @Autowired private FlightRepository flightRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PassengerRepository passengerRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (aircraftRepository.count() > 0) {
            System.out.println("[DataLoader] Database already seeded. Skipping.");
            return;
        }

        System.out.println("[DataLoader] Seeding database...");

        // 1. Seed Aircraft (from original AllAircrafts class)
        seedAircraft();

        // 2. Seed Flights (from airline_data.csv)
        seedFlights();

        // 3. Create default admin account
        createAdminUser();

        // 4. Seed Passengers and create user accounts for them
        seedPassengers();

        System.out.println("[DataLoader] Database seeding complete!");
    }

    private void seedAircraft() {
        aircraftRepository.save(new Aircraft("Boeing 747", 450, 20, 412000, 12));
        aircraftRepository.save(new Aircraft("Boeing 777", 350, 15, 351500, 10));
        aircraftRepository.save(new Aircraft("Boeing 737", 180, 10, 79015, 6));
        aircraftRepository.save(new Aircraft("Airbus A380", 500, 20, 560000, 15));
        aircraftRepository.save(new Aircraft("Airbus A340", 370, 15, 381200, 11));
        aircraftRepository.save(new Aircraft("Airbus A320", 150, 10, 79000, 4));
        System.out.println("[DataLoader] ✓ 6 aircraft loaded");
    }

    private void seedFlights() {
        try {
            ClassPathResource resource = new ClassPathResource("data/airline_data.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String line;
            boolean isFirstLine = true;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    double distance = 0;
                    try { distance = Double.parseDouble(parts[8].trim()); } catch (Exception ignored) {}

                    Flight flight = new Flight(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim(),
                        distance, parts[9].trim()
                    );
                    flightRepository.save(flight);
                    count++;
                }
            }
            reader.close();
            System.out.println("[DataLoader] ✓ " + count + " flights loaded");
        } catch (Exception e) {
            System.err.println("[DataLoader] Error loading flights: " + e.getMessage());
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"),
                    "admin@aerocode.com", "System Administrator", Role.ADMIN);
            userRepository.save(admin);
            System.out.println("[DataLoader] ✓ Admin account created (admin / admin123)");
        }
    }

    private void seedPassengers() {
        try {
            ClassPathResource resource = new ClassPathResource("data/passenger_data.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String line;
            boolean isFirstLine = true;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                line = line.trim();
                if (line.isEmpty()) continue;

                // Remove trailing comma if present
                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }

                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String name = parts[0].trim();
                    int age = 0;
                    try { age = Integer.parseInt(parts[1].trim()); } catch (Exception ignored) {}
                    long mobNum = 0;
                    try { mobNum = Long.parseLong(parts[2].trim()); } catch (Exception ignored) {}
                    String email = parts[3].trim();
                    String gender = parts[4].trim();
                    int passportNum = 0;
                    try { passportNum = Integer.parseInt(parts[5].trim()); } catch (Exception ignored) {}
                    String flightNum = parts[6].trim();

                    // Create a user account for this passenger
                    String username = name.toLowerCase().replaceAll("\\s+", ".");
                    if (!userRepository.existsByUsername(username)) {
                        User user = new User(username, passwordEncoder.encode("password123"),
                                email, name, Role.PASSENGER);
                        user = userRepository.save(user);

                        Passenger passenger = new Passenger(name, age, mobNum, email, gender, passportNum);
                        passenger.setUser(user);
                        passenger = passengerRepository.save(passenger);

                        // Create a booking for the flight they were on
                        Flight flight = flightRepository.findByFlightNumber(flightNum).orElse(null);
                        if (flight != null) {
                            double price = calculatePrice(flight);
                            Booking booking = new Booking(passenger, flight, price);
                            bookingRepository.save(booking);
                        }
                        count++;
                    }
                }
            }
            reader.close();
            System.out.println("[DataLoader] ✓ " + count + " passengers loaded with bookings");
        } catch (Exception e) {
            System.err.println("[DataLoader] Error loading passengers: " + e.getMessage());
        }
    }

    private double calculatePrice(Flight flight) {
        final double FUEL_PRICE = 1120.0;
        Aircraft aircraft = aircraftRepository.findByNameIgnoreCase(flight.getAircraft()).orElse(null);
        if (aircraft != null) {
            return (FUEL_PRICE * aircraft.getFuelConsumption() * flight.getDistance())
                    / (aircraft.getPassengerCapacity() + aircraft.getCrewCapacity());
        }
        return 0;
    }
}
