package com.fittrack.backend.repository;

import com.fittrack.backend.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    List<Workout> findByUserIdOrderByWorkoutDateDesc(UUID userId);
    List<Workout> findByUserIdAndWorkoutDateBetweenOrderByWorkoutDateDesc(UUID userId, LocalDateTime start, LocalDateTime end);
}
