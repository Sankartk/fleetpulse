package com.fleetpulse.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "telematics_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelematicsReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "reading_timestamp", nullable = false)
    private LocalDateTime readingTimestamp;

    @Column(nullable = false)
    private Double mileage;

    @Column(name = "fuel_level_percent")
    private Double fuelLevelPercent;

    @Column(name = "engine_hours")
    private Double engineHours;

    @Column(name = "average_speed_kmh")
    private Double averageSpeedKmh;

    @Column(name = "fuel_consumed_liters")
    private Double fuelConsumedLiters;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
