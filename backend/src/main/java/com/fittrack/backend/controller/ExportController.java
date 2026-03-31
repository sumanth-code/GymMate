package com.fittrack.backend.controller;

import com.fittrack.backend.entity.Workout;
import com.fittrack.backend.repository.WorkoutRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    private final WorkoutRepository workoutRepository;

    public ExportController(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @GetMapping("/csv/{userId}")
    public ResponseEntity<String> exportWorkoutsToCsv(@PathVariable UUID userId) {
        List<Workout> workouts = workoutRepository.findByUserIdOrderByWorkoutDateDesc(userId); 
        
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Exercise Name,Weight (kg),Sets,Reps,Calories Burned,Date\n");
        
        for (Workout w : workouts) {
            csvBuilder.append(w.getName()).append(",")
                      .append(w.getWeightLifted()).append(",")
                      .append(w.getSets()).append(",")
                      .append(w.getReps()).append(",")
                      .append(w.getCaloriesBurned()).append(",")
                      .append(w.getWorkoutDate()).append("\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fittrack_history.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBuilder.toString());
    }
}
