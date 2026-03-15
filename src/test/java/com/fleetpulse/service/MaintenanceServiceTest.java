package com.fleetpulse.service;

import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.enums.MaintenanceType;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.enums.VehicleType;
import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.model.MaintenanceRecord;
import com.fleetpulse.domain.model.Vehicle;
import com.fleetpulse.domain.repository.MaintenanceRepository;
import com.fleetpulse.domain.repository.VehicleRepository;
import com.fleetpulse.exception.BusinessException;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.impl.MaintenanceServiceImpl;
import com.fleetpulse.web.dto.CreateMaintenanceRequest;
import com.fleetpulse.web.dto.MaintenanceRecordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MaintenanceService unit tests")
class MaintenanceServiceTest {

    @Mock MaintenanceRepository maintenanceRepository;
    @Mock VehicleRepository     vehicleRepository;
    @InjectMocks MaintenanceServiceImpl sut;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .id(1L).registrationNumber("FP-TRK-001")
                .make("Ford").model("F-350").year(2021)
                .vehicleType(VehicleType.TRUCK)
                .status(VehicleStatus.ACTIVE)
                .fuelType(FuelType.DIESEL)
                .build();
    }

    @Test
    @DisplayName("schedule throws BusinessException for past date")
    void schedule_pastDate_throws() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        CreateMaintenanceRequest req = new CreateMaintenanceRequest(
                1L, MaintenanceType.OIL_CHANGE,
                LocalDate.now().minusDays(1), null, null, null, null);

        assertThatThrownBy(() -> sut.schedule(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("past");
    }

    @Test
    @DisplayName("schedule creates record with SCHEDULED status")
    void schedule_futureDate_createsRecord() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        LocalDate future = LocalDate.now().plusDays(10);

        MaintenanceRecord saved = MaintenanceRecord.builder()
                .id(100L).vehicle(vehicle)
                .maintenanceType(MaintenanceType.OIL_CHANGE)
                .status(MaintenanceStatus.SCHEDULED)
                .scheduledDate(future).build();
        when(maintenanceRepository.save(any())).thenReturn(saved);

        CreateMaintenanceRequest req = new CreateMaintenanceRequest(
                1L, MaintenanceType.OIL_CHANGE, future, null, null, null, null);

        MaintenanceRecordDto result = sut.schedule(req);

        assertThat(result.status()).isEqualTo(MaintenanceStatus.SCHEDULED);
        assertThat(result.vehicleRegistration()).isEqualTo("FP-TRK-001");
    }

    @Test
    @DisplayName("complete sets status to COMPLETED and stamps completedDate")
    void complete_validRecord_completesIt() {
        MaintenanceRecord record = MaintenanceRecord.builder()
                .id(1L).vehicle(vehicle)
                .maintenanceType(MaintenanceType.TIRE_ROTATION)
                .status(MaintenanceStatus.SCHEDULED)
                .scheduledDate(LocalDate.now().minusDays(1))
                .build();
        when(maintenanceRepository.findById(1L)).thenReturn(Optional.of(record));
        when(maintenanceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MaintenanceRecordDto result = sut.complete(1L, new BigDecimal("95.00"), "Mike Torres");

        assertThat(result.status()).isEqualTo(MaintenanceStatus.COMPLETED);
        assertThat(result.completedDate()).isEqualTo(LocalDate.now());
        assertThat(result.costAmount()).isEqualByComparingTo("95.00");
    }

    @Test
    @DisplayName("complete throws BusinessException when already completed")
    void complete_alreadyCompleted_throws() {
        MaintenanceRecord record = MaintenanceRecord.builder()
                .id(2L).vehicle(vehicle)
                .status(MaintenanceStatus.COMPLETED)
                .scheduledDate(LocalDate.now()).build();
        when(maintenanceRepository.findById(2L)).thenReturn(Optional.of(record));

        assertThatThrownBy(() -> sut.complete(2L, null, null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already completed");
    }

    @Test
    @DisplayName("markOverdueRecords updates status to OVERDUE for past-due records")
    void markOverdueRecords_updatesPastDueToOverdue() {
        MaintenanceRecord overdue = MaintenanceRecord.builder()
                .id(3L).vehicle(vehicle)
                .status(MaintenanceStatus.SCHEDULED)
                .scheduledDate(LocalDate.now().minusDays(5)).build();
        when(maintenanceRepository.findOverdueRecords(any())).thenReturn(List.of(overdue));
        when(maintenanceRepository.saveAll(anyList())).thenReturn(List.of(overdue));

        sut.markOverdueRecords();

        assertThat(overdue.getStatus()).isEqualTo(MaintenanceStatus.OVERDUE);
        verify(maintenanceRepository).saveAll(anyList());
    }
}
