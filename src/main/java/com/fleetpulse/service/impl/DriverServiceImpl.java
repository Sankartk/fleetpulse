package com.fleetpulse.service.impl;

import com.fleetpulse.domain.model.Driver;
import com.fleetpulse.domain.repository.DriverRepository;
import com.fleetpulse.exception.BusinessException;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.DriverService;
import com.fleetpulse.web.dto.CreateDriverRequest;
import com.fleetpulse.web.dto.DriverDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public List<DriverDto> findAll() {
        return driverRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public DriverDto findById(Long id) {
        return driverRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", id));
    }

    @Override
    @Transactional
    public DriverDto create(CreateDriverRequest request) {
        if (driverRepository.existsByEmployeeId(request.employeeId())) {
            throw new BusinessException("Employee ID '%s' already registered".formatted(request.employeeId()));
        }
        if (driverRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new BusinessException("License number '%s' already registered".formatted(request.licenseNumber()));
        }

        Driver driver = Driver.builder()
                .employeeId(request.employeeId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .licenseNumber(request.licenseNumber())
                .licenseExpiryDate(request.licenseExpiryDate())
                .contactPhone(request.contactPhone())
                .status(request.status())
                .build();

        return toDto(driverRepository.save(driver));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver", id);
        }
        driverRepository.deleteById(id);
    }

    @Override
    public List<DriverDto> findAvailable() {
        return driverRepository.findAvailableDrivers().stream().map(this::toDto).toList();
    }

    private DriverDto toDto(Driver d) {
        return new DriverDto(
                d.getId(),
                d.getEmployeeId(),
                d.getFirstName(),
                d.getLastName(),
                d.getFullName(),
                d.getLicenseNumber(),
                d.getLicenseExpiryDate(),
                d.getContactPhone(),
                d.getStatus(),
                d.getCreatedAt()
        );
    }
}
