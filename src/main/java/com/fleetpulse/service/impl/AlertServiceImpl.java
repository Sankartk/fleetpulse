package com.fleetpulse.service.impl;

import com.fleetpulse.domain.enums.AlertSeverity;
import com.fleetpulse.domain.enums.AlertType;
import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.model.Alert;
import com.fleetpulse.domain.model.MaintenanceRecord;
import com.fleetpulse.domain.repository.AlertRepository;
import com.fleetpulse.domain.repository.MaintenanceRepository;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.AlertService;
import com.fleetpulse.web.dto.AlertDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertServiceImpl implements AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    public List<AlertDto> findAll() {
        return alertRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<AlertDto> findUnresolved() {
        return alertRepository.findByResolvedFalseOrderByCreatedAtDesc().stream()
                .map(this::toDto).toList();
    }

    @Override
    @Transactional
    public AlertDto resolve(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", id));
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        return toDto(alertRepository.save(alert));
    }

    @Override
    public long countUnresolved() {
        return alertRepository.countByResolvedFalse();
    }

    @Override
    @Transactional
    public void generateMaintenanceAlerts() {
        List<MaintenanceRecord> overdueRecords =
                maintenanceRepository.findOverdueRecords(LocalDate.now());

        for (MaintenanceRecord record : overdueRecords) {
            List<Alert> existing = alertRepository.findActiveAlertsForVehicleAndType(
                    record.getVehicle().getId(), AlertType.MAINTENANCE_DUE);

            if (existing.isEmpty()) {
                Alert alert = Alert.builder()
                        .vehicle(record.getVehicle())
                        .alertType(AlertType.MAINTENANCE_DUE)
                        .severity(AlertSeverity.HIGH)
                        .message("%s %s on vehicle %s is OVERDUE since %s".formatted(
                                record.getMaintenanceType(),
                                record.getMaintenanceType().name().replace("_", " ").toLowerCase(),
                                record.getVehicle().getRegistrationNumber(),
                                record.getScheduledDate()))
                        .build();
                alertRepository.save(alert);
                log.info("Generated MAINTENANCE_DUE alert for vehicle {}",
                        record.getVehicle().getRegistrationNumber());
            }
        }
    }

    private AlertDto toDto(Alert a) {
        return new AlertDto(
                a.getId(),
                a.getVehicle().getId(),
                a.getVehicle().getRegistrationNumber(),
                a.getAlertType(),
                a.getSeverity(),
                a.getMessage(),
                a.isResolved(),
                a.getCreatedAt(),
                a.getResolvedAt()
        );
    }
}
