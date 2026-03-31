package com.fittrack.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workouts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "weight_lifted")
    private BigDecimal weightLifted;

    @Column(name = "reps", nullable = false)
    private Integer reps;

    @Column(name = "sets", nullable = false)
    private Integer sets;

    @Column(name = "calories_burned")
    private BigDecimal caloriesBurned;

    @CreationTimestamp
    @Column(name = "workout_date")
    private LocalDateTime workoutDate;
}
