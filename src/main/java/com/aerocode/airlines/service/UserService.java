package com.aerocode.airlines.service;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.repository.PassengerRepository;
import com.aerocode.airlines.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String password, String email, String fullName,
                              int age, long mobileNum, String gender) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(Role.PASSENGER);
        user = userRepository.save(user);

        Passenger passenger = new Passenger();
        passenger.setName(fullName);
        passenger.setAge(age);
        passenger.setMobileNum(mobileNum);
        passenger.setEmail(email);
        passenger.setGender(gender);
        passenger.setPassportNum(0);
        passenger.setUser(user);
        passengerRepository.save(passenger);

        return user;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
