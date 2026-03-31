package com.fittrack.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AuthResponseDto {
    private String token; // Pure Spring Boot Custom generated JWT authorization wrapper 
    private UUID userId;
    private String message;
}
