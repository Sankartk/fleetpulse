package com.fleetpulse.service.impl;

import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.model.MaintenanceRecord;
import com.fleetpulse.domain.model.Vehicle;
import com.fleetpulse.domain.repository.MaintenanceRepository;
import com.fleetpulse.domain.repository.VehicleRepository;
import com.fleetpulse.exception.BusinessException;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.MaintenanceService;
import com.fleetpulse.web.dto.CreateMaintenanceRequest;
import com.fleetpulse.web.dto.MaintenanceRecordDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceServiceImpl implements MaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public List<MaintenanceRecordDto> findAll() {
        return maintenanceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MaintenanceRecordDto findById(Long id) {
        return maintenanceRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceRecord", id));
    }

    @Override
    @Transactional
    public MaintenanceRecordDto schedule(CreateMaintenanceRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", request.vehicleId()));

        if (request.scheduledDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Scheduled date cannot be in the past");
        }

        MaintenanceRecord record = MaintenanceRecord.builder()
                .vehicle(vehicle)
                .maintenanceType(request.maintenanceType())
                .status(MaintenanceStatus.SCHEDULED)
                .scheduledDate(request.scheduledDate())
                .mileageAtService(request.mileageAtService())
                .costAmount(request.estimatedCost())
                .technicianName(request.technicianName())
                .notes(request.notes())
                .build();

        MaintenanceRecord saved = maintenanceRepository.save(record);
        log.info("Scheduled {} for vehicle {}", request.maintenanceType(), vehicle.getRegistrationNumber());
        return toDto(saved);
    }

    @Override
    @Transactional
    public MaintenanceRecordDto complete(Long id, BigDecimal actualCost, String technicianName) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaintenanceRecord", id));

        if (record.getStatus() == MaintenanceStatus.COMPLETED) {
            throw new BusinessException("Maintenance record is already completed");
        }

        record.setStatus(MaintenanceStatus.COMPLETED);
        record.setCompletedDate(LocalDate.now());
        if (actualCost != null) record.setCostAmount(actualCost);
        if (technicianName != null) record.setTechnicianName(technicianName);

        // Update vehicle mileage if provided
        if (record.getMileageAtService() != null) {
            Vehicle vehicle = record.getVehicle();
            if (vehicle.getCurrentMileage() == null ||
                    record.getMileageAtService() > vehicle.getCurrentMileage()) {
                vehicle.setCurrentMileage(record.getMileageAtService());
                vehicleRepository.save(vehicle);
            }
        }

        return toDto(maintenanceRepository.save(record));
    }

    @Override
    public List<MaintenanceRecordDto> findOverdue() {
        return maintenanceRepository.findOverdueRecords(LocalDate.now())
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<MaintenanceRecordDto> findUpcomingThirtyDays() {
        LocalDate today = LocalDate.now();
        return maintenanceRepository.findUpcoming(today, today.plusDays(30))
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<MaintenanceRecordDto> findByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle", vehicleId);
        }
        return maintenanceRepository.findByVehicleIdOrderByScheduledDateDesc(vehicleId)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public void markOverdueRecords() {
        List<MaintenanceRecord> overdue = maintenanceRepository.findOverdueRecords(LocalDate.now());
        overdue.forEach(record -> record.setStatus(MaintenanceStatus.OVERDUE));
        maintenanceRepository.saveAll(overdue);
        if (!overdue.isEmpty()) {
            log.info("Marked {} maintenance records as OVERDUE", overdue.size());
        }
    }

    private MaintenanceRecordDto toDto(MaintenanceRecord m) {
        Vehicle v = m.getVehicle();
        return new MaintenanceRecordDto(
                m.getId(),
                v != null ? v.getId() : null,
                v != null ? v.getRegistrationNumber() : null,
                v != null ? "%s %s".formatted(v.getMake(), v.getModel()) : null,
                m.getMaintenanceType(),
                m.getStatus(),
                m.getScheduledDate(),
                m.getCompletedDate(),
                m.getMileageAtService(),
                m.getCostAmount(),
                m.getTechnicianName(),
                m.getNotes(),
                m.getCreatedAt()
        );
    }
}
