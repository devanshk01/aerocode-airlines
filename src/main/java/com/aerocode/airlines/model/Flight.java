package com.aerocode.airlines.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number is required")
    @Column(unique = true, nullable = false)
    private String flightNumber;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Departure city is required")
    @Column(name = "departure_city")
    private String fromCity;

    @NotBlank(message = "Arrival city is required")
    @Column(name = "arrival_city")
    private String toCity;

    @NotBlank(message = "Departure time is required")
    private String departure;

    @NotBlank(message = "Arrival time is required")
    private String arrival;

    @NotBlank(message = "Aircraft is required")
    private String aircraft;

    @NotBlank(message = "Flight type is required")
    private String type;

    @Positive(message = "Distance must be positive")
    private double distance;

    private String flightDuration;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public Flight() {}

    public Flight(String flightNumber, String date, String fromCity, String toCity,
                  String departure, String arrival, String aircraft, String type,
                  double distance, String flightDuration) {
        this.flightNumber = flightNumber;
        this.date = date;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departure = departure;
        this.arrival = arrival;
        this.aircraft = aircraft;
        this.type = type;
        this.distance = distance;
        this.flightDuration = flightDuration;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }

    public String getAircraft() { return aircraft; }
    public void setAircraft(String aircraft) { this.aircraft = aircraft; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public String getFlightDuration() { return flightDuration; }
    public void setFlightDuration(String flightDuration) { this.flightDuration = flightDuration; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
