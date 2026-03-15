package com.fleetpulse.web.dto;

import java.util.List;

public record DashboardSummaryDto(
        long totalVehicles,
        long activeVehicles,
        long maintenanceVehicles,
        long retiredVehicles,
        long overdueMaintenanceCount,
        long upcomingMaintenanceCount,
        long unresolvedAlertCount,
        long criticalAlertCount,
        List<String> monthlyCostLabels,
        List<Double> monthlyCostValues,
        List<AlertDto> recentAlerts,
        List<MaintenanceRecordDto> upcomingMaintenance
) {}
