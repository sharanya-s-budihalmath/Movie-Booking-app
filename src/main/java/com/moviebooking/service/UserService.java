package com.moviebooking.service;

import com.moviebooking.model.User;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        log.info("Registering new user: {}", user.getEmail());
        return userRepository.save(user);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users (admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user (admin)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}
