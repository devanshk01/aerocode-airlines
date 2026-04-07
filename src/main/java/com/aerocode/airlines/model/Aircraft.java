package com.aerocode.airlines.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Aircraft name is required")
    @Column(unique = true, nullable = false)
    private String name;

    @Min(value = 1, message = "Passenger capacity must be at least 1")
    private int passengerCapacity;

    @Min(value = 1, message = "Crew capacity must be at least 1")
    private int crewCapacity;

    @Positive(message = "Max weight must be positive")
    private double totalMaxWeightCapacity;

    @Positive(message = "Fuel consumption must be positive")
    private double fuelConsumption;

    public Aircraft() {}

    public Aircraft(String name, int passengerCapacity, int crewCapacity,
                    double totalMaxWeightCapacity, double fuelConsumption) {
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.crewCapacity = crewCapacity;
        this.totalMaxWeightCapacity = totalMaxWeightCapacity;
        this.fuelConsumption = fuelConsumption;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPassengerCapacity() { return passengerCapacity; }
    public void setPassengerCapacity(int passengerCapacity) { this.passengerCapacity = passengerCapacity; }

    public int getCrewCapacity() { return crewCapacity; }
    public void setCrewCapacity(int crewCapacity) { this.crewCapacity = crewCapacity; }

    public double getTotalMaxWeightCapacity() { return totalMaxWeightCapacity; }
    public void setTotalMaxWeightCapacity(double totalMaxWeightCapacity) { this.totalMaxWeightCapacity = totalMaxWeightCapacity; }

    public double getFuelConsumption() { return fuelConsumption; }
    public void setFuelConsumption(double fuelConsumption) { this.fuelConsumption = fuelConsumption; }
}
