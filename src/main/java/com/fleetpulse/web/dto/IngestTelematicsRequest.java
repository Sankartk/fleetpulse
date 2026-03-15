package com.fleetpulse.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record IngestTelematicsRequest(
        @NotNull LocalDateTime readingTimestamp,
        @NotNull @PositiveOrZero Double mileage,
        @Min(0) @Max(100) Double fuelLevelPercent,
        @PositiveOrZero Double engineHours,
        @PositiveOrZero Double averageSpeedKmh,
        @PositiveOrZero Double fuelConsumedLiters
) {}
