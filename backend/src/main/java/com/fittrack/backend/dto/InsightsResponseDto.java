package com.fittrack.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class InsightsResponseDto {
    private long totalWorkouts;
    private BigDecimal totalCaloriesBurned;
    private Map<String, BigDecimal> maxWeightPerExercise;
}
