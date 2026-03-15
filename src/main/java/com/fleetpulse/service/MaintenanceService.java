package com.fleetpulse.service;

import com.fleetpulse.web.dto.CreateMaintenanceRequest;
import com.fleetpulse.web.dto.MaintenanceRecordDto;

import java.math.BigDecimal;
import java.util.List;

public interface MaintenanceService {

    List<MaintenanceRecordDto> findAll();

    MaintenanceRecordDto findById(Long id);

    MaintenanceRecordDto schedule(CreateMaintenanceRequest request);

    MaintenanceRecordDto complete(Long id, BigDecimal actualCost, String technicianName);

    List<MaintenanceRecordDto> findOverdue();

    List<MaintenanceRecordDto> findUpcomingThirtyDays();

    List<MaintenanceRecordDto> findByVehicleId(Long vehicleId);

    /** Called by scheduler to mark past-scheduled records as overdue. */
    void markOverdueRecords();
}
