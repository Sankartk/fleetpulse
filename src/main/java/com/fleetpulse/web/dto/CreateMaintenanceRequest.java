package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.MaintenanceType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMaintenanceRequest(
        @NotNull(message = "Vehicle ID is required")
        Long vehicleId,

        @NotNull(message = "Maintenance type is required")
        MaintenanceType maintenanceType,

        @NotNull(message = "Scheduled date is required")
        LocalDate scheduledDate,

        @PositiveOrZero
        Double mileageAtService,

        @DecimalMin(value = "0.0")
        BigDecimal estimatedCost,

        @Size(max = 100)
        String technicianName,

        @Size(max = 1000)
        String notes
) {}
