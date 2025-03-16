package com.example.employee_payroll_app.Services;

import com.example.employee_payroll_app.dto.UserDTO;
import com.example.employee_payroll_app.model.User;
import com.example.employee_payroll_app.Repositories.UserRepository;
import com.example.employee_payroll_app.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register User
    @Override
    public String registerUser(UserDTO userdto) {
        log.info("Registering new user: {}", userdto.getEmail());
        if (userRepository.existsByEmail(userdto.getEmail())) {
            return "Email is already in use!";
        }

        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setEmail(userdto.getEmail());
        user.setPassword(passwordEncoder.encode(userdto.getPassword())); // Encrypt password
        userRepository.save(user);

        log.info("User {} registered successfully.", user.getEmail());
        return "User registered successfully!";
    }

    // Authenticate User and Generate Token
    @Override
    public String authenticateUser(String email, String password) {
        log.info("Login attempt for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.warn("Login failed: No user found for email: {}", email);
            return "User not found!";
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed: Incorrect password for email: {}", email);
            return "Invalid email or password!";
        }

        log.info("Login successful for user: {}", email);
        return jwtUtil.generateToken(email);
    }
}

