package com.fittrack.backend.controller;

import com.fittrack.backend.dto.WorkoutRequestDto;
import com.fittrack.backend.dto.WorkoutResponseDto;
import com.fittrack.backend.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {
    
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutResponseDto> createWorkout(@Valid @RequestBody WorkoutRequestDto request) {
        return ResponseEntity.ok(workoutService.createWorkout(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutResponseDto>> getUserWorkouts(@PathVariable String userId) {
        return ResponseEntity.ok(workoutService.getUserWorkouts(UUID.fromString(userId)));
    }

    @GetMapping("/insights/{userId}")
    public ResponseEntity<com.fittrack.backend.dto.InsightsResponseDto> getInsights(@PathVariable String userId) {
        return ResponseEntity.ok(workoutService.getInsights(UUID.fromString(userId)));
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String workoutId) {
        workoutService.deleteWorkout(UUID.fromString(workoutId));
        return ResponseEntity.noContent().build();
    }
}
