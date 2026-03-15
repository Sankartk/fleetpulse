package com.fleetpulse.service;

import com.fleetpulse.domain.enums.DriverStatus;
import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.enums.VehicleType;
import com.fleetpulse.domain.model.Driver;
import com.fleetpulse.domain.model.Vehicle;
import com.fleetpulse.domain.repository.DriverRepository;
import com.fleetpulse.domain.repository.VehicleRepository;
import com.fleetpulse.exception.BusinessException;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.impl.VehicleServiceImpl;
import com.fleetpulse.web.dto.CreateVehicleRequest;
import com.fleetpulse.web.dto.VehicleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehicleService unit tests")
class VehicleServiceTest {

    @Mock VehicleRepository vehicleRepository;
    @Mock DriverRepository  driverRepository;
    @InjectMocks VehicleServiceImpl sut;

    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        sampleVehicle = Vehicle.builder()
                .id(1L)
                .registrationNumber("FP-TRK-001")
                .make("Ford").model("F-350").year(2021)
                .vehicleType(VehicleType.TRUCK)
                .status(VehicleStatus.ACTIVE)
                .fuelType(FuelType.DIESEL)
                .currentMileage(48000.0)
                .build();
    }

    @Test
    @DisplayName("findAll returns DTOs for all vehicles")
    void findAll_returnsMappedDtos() {
        when(vehicleRepository.findAllWithDrivers()).thenReturn(List.of(sampleVehicle));

        List<VehicleDto> result = sut.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).registrationNumber()).isEqualTo("FP-TRK-001");
        assertThat(result.get(0).make()).isEqualTo("Ford");
    }

    @Test
    @DisplayName("findById throws ResourceNotFoundException for unknown id")
    void findById_unknownId_throws() {
        when(vehicleRepository.findByIdWithDriver(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("create throws BusinessException when registration already exists")
    void create_duplicateRegistration_throws() {
        when(vehicleRepository.existsByRegistrationNumber("FP-TRK-001")).thenReturn(true);

        CreateVehicleRequest request = new CreateVehicleRequest(
                "FP-TRK-001", "Ford", "F-350", 2021,
                VehicleType.TRUCK, FuelType.DIESEL, LocalDate.now(), 0.0, null, null);

        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("create persists new vehicle with ACTIVE status")
    void create_validRequest_savesVehicle() {
        when(vehicleRepository.existsByRegistrationNumber(anyString())).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> {
            Vehicle v = inv.getArgument(0);
            v = Vehicle.builder()
                    .id(42L)
                    .registrationNumber(v.getRegistrationNumber())
                    .make(v.getMake()).model(v.getModel()).year(v.getYear())
                    .vehicleType(v.getVehicleType())
                    .status(VehicleStatus.ACTIVE)
                    .fuelType(v.getFuelType())
                    .currentMileage(v.getCurrentMileage())
                    .build();
            return v;
        });

        CreateVehicleRequest request = new CreateVehicleRequest(
                "FP-NEW-009", "Toyota", "Hilux", 2023,
                VehicleType.PICKUP, FuelType.DIESEL, null, 0.0, 5000.0, null);

        VehicleDto result = sut.create(request);

        assertThat(result.id()).isEqualTo(42L);
        assertThat(result.status()).isEqualTo(VehicleStatus.ACTIVE);
        verify(vehicleRepository).save(argThat(v -> v.getRegistrationNumber().equals("FP-NEW-009")));
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when vehicle not found")
    void delete_unknownId_throws() {
        when(vehicleRepository.existsById(77L)).thenReturn(false);
        assertThatThrownBy(() -> sut.delete(77L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("assignDriver throws BusinessException for retired vehicle")
    void assignDriver_retiredVehicle_throws() {
        Vehicle retired = Vehicle.builder().id(5L).status(VehicleStatus.RETIRED).build();
        Driver driver = Driver.builder().id(1L).status(DriverStatus.ACTIVE).build();

        when(vehicleRepository.findById(5L)).thenReturn(Optional.of(retired));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        assertThatThrownBy(() -> sut.assignDriver(5L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("retired");
    }
}
