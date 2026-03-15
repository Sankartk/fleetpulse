package com.fleetpulse.service;

import com.fleetpulse.web.dto.CreateDriverRequest;
import com.fleetpulse.web.dto.DriverDto;

import java.util.List;

public interface DriverService {

    List<DriverDto> findAll();

    DriverDto findById(Long id);

    DriverDto create(CreateDriverRequest request);

    void delete(Long id);

    List<DriverDto> findAvailable();
}
