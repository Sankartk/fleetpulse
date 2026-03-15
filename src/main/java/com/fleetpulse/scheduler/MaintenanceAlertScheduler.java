package com.fleetpulse.scheduler;

import com.fleetpulse.service.AlertService;
import com.fleetpulse.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceAlertScheduler.class);

    private final MaintenanceService maintenanceService;
    private final AlertService alertService;

    /**
     * Runs every hour: marks any SCHEDULED records past their due date as OVERDUE,
     * then generates MAINTENANCE_DUE alerts for each affected vehicle.
     */
    @Scheduled(fixedRateString = "${fleetpulse.scheduler.overdue-check-ms:3600000}")
    public void runOverdueCheck() {
        log.debug("Running scheduled overdue maintenance check...");
        maintenanceService.markOverdueRecords();
        alertService.generateMaintenanceAlerts();
    }
}
