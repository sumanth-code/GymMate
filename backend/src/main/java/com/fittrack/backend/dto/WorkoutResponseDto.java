package com.fittrack.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WorkoutResponseDto {
    private UUID id;
    private String name;
    private BigDecimal weightLifted;
    private Integer reps;
    private Integer sets;
    private BigDecimal caloriesBurned;
    private LocalDateTime workoutDate;
}
