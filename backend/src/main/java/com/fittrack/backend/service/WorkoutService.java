package com.fittrack.backend.service;

import com.fittrack.backend.dto.WorkoutRequestDto;
import com.fittrack.backend.dto.WorkoutResponseDto;
import com.fittrack.backend.entity.User;
import com.fittrack.backend.entity.Workout;
import com.fittrack.backend.repository.UserRepository;
import com.fittrack.backend.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public WorkoutResponseDto createWorkout(WorkoutRequestDto request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BigDecimal caloriesBurned = calculateCaloriesBurned(request, user);

        Workout workout = Workout.builder()
            .user(user)
            .name(request.getName())
            .weightLifted(request.getWeightLifted())
            .reps(request.getReps())
            .sets(request.getSets())
            .caloriesBurned(caloriesBurned)
            .workoutDate(LocalDateTime.now())
            .build();

        workout = workoutRepository.save(workout);
        return mapToDto(workout);
    }

    public List<WorkoutResponseDto> getUserWorkouts(UUID userId) {
        return workoutRepository.findByUserIdOrderByWorkoutDateDesc(userId)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public void deleteWorkout(UUID workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    public com.fittrack.backend.dto.InsightsResponseDto getInsights(UUID userId) {
        List<Workout> workouts = workoutRepository.findByUserIdOrderByWorkoutDateDesc(userId);
        
        com.fittrack.backend.dto.InsightsResponseDto insights = new com.fittrack.backend.dto.InsightsResponseDto();
        insights.setTotalWorkouts(workouts.size());
        
        BigDecimal totalCalories = workouts.stream()
            .map(w -> w.getCaloriesBurned() != null ? w.getCaloriesBurned() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        insights.setTotalCaloriesBurned(totalCalories);
        
        java.util.Map<String, BigDecimal> maxWeights = workouts.stream()
            .filter(w -> w.getWeightLifted() != null)
            .collect(Collectors.toMap(
                Workout::getName,
                Workout::getWeightLifted,
                (oldVal, newVal) -> (oldVal.compareTo(newVal) > 0) ? oldVal : newVal
            ));
        
        insights.setMaxWeightPerExercise(maxWeights);
        return insights;
    }

    private BigDecimal calculateCaloriesBurned(WorkoutRequestDto request, User user) {
        // Basic MET calculation: Calories = MET * Weight(kg) * Time(hrs)
        double totalMinutes = request.getSets() * 1.5;
        double timeHrs = totalMinutes / 60.0;
        
        // Default MET value for general weightlifting
        double metValue = 5.0; 
        if (request.getName().toLowerCase().contains("squat")) metValue = 6.0;
        if (request.getName().toLowerCase().contains("deadlift")) metValue = 6.0;
        if (request.getName().toLowerCase().contains("cardio")) metValue = 8.0;

        double userWeight = (user.getWeightKg() != null) ? user.getWeightKg().doubleValue() : 70.0; // Assume 70kg default

        double calories = metValue * userWeight * timeHrs;
        return BigDecimal.valueOf(calories).setScale(2, RoundingMode.HALF_UP);
    }

    private WorkoutResponseDto mapToDto(Workout workout) {
        WorkoutResponseDto dto = new WorkoutResponseDto();
        dto.setId(workout.getId());
        dto.setName(workout.getName());
        dto.setWeightLifted(workout.getWeightLifted());
        dto.setReps(workout.getReps());
        dto.setSets(workout.getSets());
        dto.setCaloriesBurned(workout.getCaloriesBurned());
        dto.setWorkoutDate(workout.getWorkoutDate());
        return dto;
    }
}
