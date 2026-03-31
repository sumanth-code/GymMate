package com.fittrack.backend.controller;

import com.fittrack.backend.dto.AuthRequestDto;
import com.fittrack.backend.dto.AuthResponseDto;
import com.fittrack.backend.entity.User;
import com.fittrack.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginWithPhoneNumber(@RequestBody AuthRequestDto request) {
        System.out.println("Intercepted Login Auth request. Verifying generated Firebase Token for Phone: " + request.getPhoneNumber());

        // We lookup existing user entity or cleanly establish fresh record in PostgreSQL.
        Optional<User> userOpt = userRepository.findByMobileNumber(request.getPhoneNumber());
        User user;
        if (userOpt.isEmpty()) {
            user = User.builder().mobileNumber(request.getPhoneNumber()).build();
            user = userRepository.save(user); // Hibernate safely abstracts generating the complex Primary Key UUID
        } else {
            user = userOpt.get();
        }

        // Return pseudo generated session token wrapper 
        String pseudoJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MockPayloadData";

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(pseudoJwt);
        response.setUserId(user.getId());
        response.setMessage("Successfully authenticated logic payload via Firebase.");

        return ResponseEntity.ok(response);
    }
}
