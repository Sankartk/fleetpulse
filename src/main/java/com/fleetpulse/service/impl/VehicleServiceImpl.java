package com.fleetpulse.service.impl;

import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.model.Driver;
import com.fleetpulse.domain.model.Vehicle;
import com.fleetpulse.domain.repository.DriverRepository;
import com.fleetpulse.domain.repository.VehicleRepository;
import com.fleetpulse.exception.BusinessException;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.VehicleService;
import com.fleetpulse.web.dto.CreateVehicleRequest;
import com.fleetpulse.web.dto.UpdateVehicleRequest;
import com.fleetpulse.web.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private static final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Override
    public List<VehicleDto> findAll() {
        return vehicleRepository.findAllWithDrivers().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public VehicleDto findById(Long id) {
        return vehicleRepository.findByIdWithDriver(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
    }

    @Override
    @Transactional
    public VehicleDto create(CreateVehicleRequest request) {
        if (vehicleRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new BusinessException(
                    "Vehicle with registration '%s' already exists".formatted(request.registrationNumber()));
        }

        Vehicle vehicle = Vehicle.builder()
                .registrationNumber(request.registrationNumber().toUpperCase())
                .make(request.make())
                .model(request.model())
                .year(request.year())
                .vehicleType(request.vehicleType())
                .status(VehicleStatus.ACTIVE)
                .fuelType(request.fuelType())
                .purchaseDate(request.purchaseDate())
                .currentMileage(request.currentMileage() != null ? request.currentMileage() : 0.0)
                .nextServiceMileage(request.nextServiceMileage())
                .build();

        if (request.driverId() != null) {
            Driver driver = driverRepository.findById(request.driverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver", request.driverId()));
            vehicle.setAssignedDriver(driver);
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("Created vehicle: {}", saved.getRegistrationNumber());
        return toDto(saved);
    }

    @Override
    @Transactional
    public VehicleDto update(Long id, UpdateVehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        if (request.make() != null) vehicle.setMake(request.make());
        if (request.model() != null) vehicle.setModel(request.model());
        if (request.year() != null) vehicle.setYear(request.year());
        if (request.vehicleType() != null) vehicle.setVehicleType(request.vehicleType());
        if (request.status() != null) vehicle.setStatus(request.status());
        if (request.fuelType() != null) vehicle.setFuelType(request.fuelType());
        if (request.purchaseDate() != null) vehicle.setPurchaseDate(request.purchaseDate());
        if (request.currentMileage() != null) vehicle.setCurrentMileage(request.currentMileage());
        if (request.nextServiceMileage() != null) vehicle.setNextServiceMileage(request.nextServiceMileage());

        return toDto(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", id);
        }
        vehicleRepository.deleteById(id);
        log.info("Deleted vehicle id: {}", id);
    }

    @Override
    @Transactional
    public VehicleDto assignDriver(Long vehicleId, Long driverId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", driverId));

        if (vehicle.getStatus() == VehicleStatus.RETIRED) {
            throw new BusinessException("Cannot assign driver to a retired vehicle");
        }

        vehicle.setAssignedDriver(driver);
        return toDto(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public VehicleDto unassignDriver(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));
        vehicle.setAssignedDriver(null);
        return toDto(vehicleRepository.save(vehicle));
    }

    @Override
    public List<VehicleDto> findByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream()
                .map(this::toDto)
                .toList();
    }

    private VehicleDto toDto(Vehicle v) {
        Driver driver = v.getAssignedDriver();
        return new VehicleDto(
                v.getId(),
                v.getRegistrationNumber(),
                v.getMake(),
                v.getModel(),
                v.getYear(),
                v.getVehicleType(),
                v.getStatus(),
                v.getFuelType(),
                v.getPurchaseDate(),
                v.getCurrentMileage(),
                v.getNextServiceMileage(),
                driver != null ? driver.getId() : null,
                driver != null ? driver.getFullName() : null,
                v.getCreatedAt(),
                v.getUpdatedAt()
        );
    }
}
