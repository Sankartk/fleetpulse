package com.fleetpulse.service;

import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.web.dto.CreateVehicleRequest;
import com.fleetpulse.web.dto.UpdateVehicleRequest;
import com.fleetpulse.web.dto.VehicleDto;

import java.util.List;

public interface VehicleService {

    List<VehicleDto> findAll();

    VehicleDto findById(Long id);

    VehicleDto create(CreateVehicleRequest request);

    VehicleDto update(Long id, UpdateVehicleRequest request);

    void delete(Long id);

    VehicleDto assignDriver(Long vehicleId, Long driverId);

    VehicleDto unassignDriver(Long vehicleId);

    List<VehicleDto> findByStatus(VehicleStatus status);
}
