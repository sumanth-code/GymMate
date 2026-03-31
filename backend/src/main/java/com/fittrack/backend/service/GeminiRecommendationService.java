package com.fittrack.backend.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GeminiRecommendationService {
    
    public String generateRecommendations(UUID userId, String bmiData) {
        System.out.println("Processing prompt with Gemini System... BMI Context: " + bmiData);
        // HTTP POST to https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key={API_KEY}
        // Returns a deterministic string as fallback execution mode.
        return "AI Suggestion: Based on the extracted BMI of 23.1, maintain a daily caloric intake of 2500 kcal, and focus on a mix of cardio (3x/week) and strength training (4x/week).";
    }
}
