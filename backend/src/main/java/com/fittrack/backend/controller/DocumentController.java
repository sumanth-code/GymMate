package com.fittrack.backend.controller;

import com.fittrack.backend.service.DocumentService;
import com.fittrack.backend.service.GeminiRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final GeminiRecommendationService aiService;

    public DocumentController(DocumentService documentService, GeminiRecommendationService aiService) {
        this.documentService = documentService;
        this.aiService = aiService;
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadBMIDocument(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        String documentUrl = documentService.uploadDocument(file, UUID.fromString(userId));
        String extractedText = documentService.extractTextFromImage(file);
        
        // Feed OCR extracted text back into to Gemini AI to get custom fitness recommendations
        String aiRecommendation = aiService.generateRecommendations(UUID.fromString(userId), extractedText);
        
        return ResponseEntity.ok(Map.of(
            "documentUrl", documentUrl,
            "extractedText", extractedText,
            "aiRecommendation", aiRecommendation,
            "message", "Document analyzed safely and custom workout suggested by AI."
        ));
    }
}
