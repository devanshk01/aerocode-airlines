package com.aerocode.airlines.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 18, message = "Must be at least 18 years old")
    @Max(value = 120, message = "Please enter a valid age")
    private int age;

    private long mobileNum;

    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Gender is required")
    private String gender;

    private int passportNum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Passenger() {}

    public Passenger(String name, int age, long mobileNum, String email,
                     String gender, int passportNum) {
        this.name = name;
        this.age = age;
        this.mobileNum = mobileNum;
        this.email = email;
        this.gender = gender;
        this.passportNum = passportNum;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public long getMobileNum() { return mobileNum; }
    public void setMobileNum(long mobileNum) { this.mobileNum = mobileNum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getPassportNum() { return passportNum; }
    public void setPassportNum(int passportNum) { this.passportNum = passportNum; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
