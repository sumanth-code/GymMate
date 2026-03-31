package com.fittrack.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class DocumentService {
    
    public String uploadDocument(MultipartFile file, UUID userId) {
        System.out.println("Processing Cloudinary upload for document of user " + userId);
        // Returns a safe mock URL until the user provides the Cloudinary API Key
        return "https://res.cloudinary.com/demo/image/upload/v1593361093/sample.jpg"; 
    }

    public String extractTextFromImage(MultipartFile file) {
        System.out.println("Mocking Tesseract OCR extraction processing (requires Tesseract installation setup)...");
        // Returns a safe mock OCR output to test the end-to-end functionality safely without crashing the backend execution
        return "BMI Report:\nName: John Doe\nHeight: 180cm\nWeight: 75kg\nBMI: 23.1";
    }
}
