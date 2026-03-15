package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.enums.VehicleType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateVehicleRequest(
        @Size(max = 100)
        String make,

        @Size(max = 100)
        String model,

        @Min(1990) @Max(2030)
        Integer year,

        VehicleType vehicleType,

        VehicleStatus status,

        FuelType fuelType,

        LocalDate purchaseDate,

        @PositiveOrZero
        Double currentMileage,

        @Positive
        Double nextServiceMileage
) {}
