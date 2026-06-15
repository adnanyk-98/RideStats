package com.ridestats.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ride")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "distance_km")
    private BigDecimal distanceKm;

    @Column(name = "elevation_gain_m")
    private BigDecimal elevationGainM;

    @Column(name = "moving_time_seconds")
    private Long movingTimeSeconds;

    @Column(name = "average_speed_kph")
    private BigDecimal averageSpeedKph;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}