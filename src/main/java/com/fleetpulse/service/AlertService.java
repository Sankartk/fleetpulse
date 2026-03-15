package com.fleetpulse.service;

import com.fleetpulse.web.dto.AlertDto;

import java.util.List;

public interface AlertService {

    List<AlertDto> findAll();

    List<AlertDto> findUnresolved();

    AlertDto resolve(Long id);

    long countUnresolved();

    /** Called by scheduler to generate alerts for overdue maintenance. */
    void generateMaintenanceAlerts();
}
