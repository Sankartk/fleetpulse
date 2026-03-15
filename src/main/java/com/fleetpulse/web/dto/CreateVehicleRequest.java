package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.enums.VehicleType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateVehicleRequest(
        @NotBlank(message = "Registration number is required")
        @Size(max = 50)
        String registrationNumber,

        @NotBlank(message = "Make is required")
        @Size(max = 100)
        String make,

        @NotBlank(message = "Model is required")
        @Size(max = 100)
        String model,

        @NotNull(message = "Year is required")
        @Min(value = 1990, message = "Year must be 1990 or later")
        @Max(value = 2030, message = "Year must be 2030 or earlier")
        Integer year,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType,

        @NotNull(message = "Fuel type is required")
        FuelType fuelType,

        LocalDate purchaseDate,

        @PositiveOrZero(message = "Mileage must be non-negative")
        Double currentMileage,

        @Positive(message = "Next service mileage must be positive")
        Double nextServiceMileage,

        Long driverId
) {}
