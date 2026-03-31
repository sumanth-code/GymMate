package com.fittrack.backend.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String phoneNumber;
    private String firebaseToken; // Firebase generated Auth JWT Token object passed from frontend OTP UI
}
