package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.DriverStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateDriverRequest(
        @NotBlank @Size(max = 50)
        String employeeId,

        @NotBlank @Size(max = 100)
        String firstName,

        @NotBlank @Size(max = 100)
        String lastName,

        @NotBlank @Size(max = 50)
        String licenseNumber,

        @NotNull @Future(message = "License must not be expired")
        LocalDate licenseExpiryDate,

        @Pattern(regexp = "^[+\\d\\-\\s()]{7,20}$", message = "Invalid phone format")
        String contactPhone,

        @NotNull
        DriverStatus status
) {}
