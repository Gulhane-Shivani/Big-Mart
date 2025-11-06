package com.example.ShoppingManagement.bigmart.controller;

import com.example.ShoppingManagement.bigmart.model.User;
import com.example.ShoppingManagement.bigmart.repository.UserRepository;
import com.example.ShoppingManagement.bigmart.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            response.put("message", "User with this email already exists");
            return response;
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword())); // <-- important
        user.setRole("USER"); // Set default role
        userRepository.save(user);

        response.put("message", "User registered successfully");
        return response;
    }


    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOptional = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(email);
                response.put("token", token);
                response.put("message", "Login successful");
            } else {
                response.put("message", "Invalid email or password");
            }
        } else {
            response.put("message", "Invalid email or password");
        }
        return response;
    }

    @GetMapping("/dashboard")
    public Map<String, String> dashboard() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Welcome to your dashboard!");
        return data;
    }
}
