package com.fittrack.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WorkoutRequestDto {
    @NotNull(message = "User ID must be provided")
    private UUID userId;

    @NotBlank(message = "Workout name cannot be empty")
    private String name;

    private BigDecimal weightLifted;

    @NotNull(message = "Reps must be specified")
    @Positive
    private Integer reps;

    @NotNull(message = "Sets must be specified")
    @Positive
    private Integer sets;
}
