package com.fleetpulse.service.impl;

import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.model.MaintenanceRecord;
import com.fleetpulse.domain.enums.AlertSeverity;
import com.fleetpulse.domain.repository.AlertRepository;
import com.fleetpulse.domain.repository.MaintenanceRepository;
import com.fleetpulse.domain.repository.VehicleRepository;
import com.fleetpulse.service.DashboardService;
import com.fleetpulse.web.dto.AlertDto;
import com.fleetpulse.web.dto.DashboardSummaryDto;
import com.fleetpulse.web.dto.MaintenanceRecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMM yyyy");

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final AlertRepository alertRepository;

    @Override
    public DashboardSummaryDto getSummary() {
        long total     = vehicleRepository.count();
        long active    = vehicleRepository.countByStatus(VehicleStatus.ACTIVE);
        long inMaint   = vehicleRepository.countByStatus(VehicleStatus.IN_MAINTENANCE);
        long retired   = vehicleRepository.countByStatus(VehicleStatus.RETIRED);
        long overdue   = maintenanceRepository.countByStatus(MaintenanceStatus.OVERDUE);
        long unresolved = alertRepository.countByResolvedFalse();
        long critical  = alertRepository.countBySeverityAndResolvedFalse(AlertSeverity.CRITICAL);

        LocalDate today = LocalDate.now();
        long upcoming  = maintenanceRepository
                .findUpcoming(today, today.plusDays(30)).size();

        // Monthly cost chart — last 6 months
        List<String> labels = buildMonthLabels(6);
        List<Double> costs  = buildMonthlyCosts(labels);

        // Recent alerts (top 5)
        List<AlertDto> recentAlerts = alertRepository
                .findByResolvedFalseOrderByCreatedAtDesc(PageRequest.of(0, 5))
                .stream().map(a -> new AlertDto(
                        a.getId(),
                        a.getVehicle().getId(),
                        a.getVehicle().getRegistrationNumber(),
                        a.getAlertType(),
                        a.getSeverity(),
                        a.getMessage(),
                        a.isResolved(),
                        a.getCreatedAt(),
                        a.getResolvedAt()))
                .toList();

        // Upcoming maintenance (next 30 days)
        List<MaintenanceRecordDto> upcomingMaint = maintenanceRepository
                .findUpcoming(today, today.plusDays(30))
                .stream().map(m -> {
                    var v = m.getVehicle();
                    return new MaintenanceRecordDto(
                            m.getId(),
                            v.getId(),
                            v.getRegistrationNumber(),
                            "%s %s".formatted(v.getMake(), v.getModel()),
                            m.getMaintenanceType(),
                            m.getStatus(),
                            m.getScheduledDate(),
                            m.getCompletedDate(),
                            m.getMileageAtService(),
                            m.getCostAmount(),
                            m.getTechnicianName(),
                            m.getNotes(),
                            m.getCreatedAt());
                }).toList();

        return new DashboardSummaryDto(
                total, active, inMaint, retired,
                overdue, upcoming, unresolved, critical,
                labels, costs,
                recentAlerts, upcomingMaint);
    }

    private List<String> buildMonthLabels(int monthsBack) {
        List<String> labels = new ArrayList<>();
        for (int i = monthsBack - 1; i >= 0; i--) {
            labels.add(LocalDate.now().minusMonths(i).format(MONTH_FMT));
        }
        return labels;
    }

    private List<Double> buildMonthlyCosts(List<String> labels) {
        LocalDate since = LocalDate.now().minusMonths(labels.size());
        Map<String, Double> costByMonth = maintenanceRepository
                .findByStatusAndCompletedDateAfter(MaintenanceStatus.COMPLETED, since)
                .stream()
                .filter(m -> m.getCostAmount() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getCompletedDate().format(MONTH_FMT),
                        Collectors.summingDouble(m -> m.getCostAmount().doubleValue())));

        return labels.stream()
                .map(l -> costByMonth.getOrDefault(l, 0.0))
                .toList();
    }
}
