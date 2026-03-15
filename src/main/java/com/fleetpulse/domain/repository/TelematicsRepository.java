package com.fleetpulse.domain.repository;

import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.model.TelematicsReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelematicsRepository extends JpaRepository<TelematicsReading, Long> {

    List<TelematicsReading> findByVehicleIdOrderByReadingTimestampDesc(Long vehicleId, Pageable pageable);

    List<TelematicsReading> findByFuelLevelPercentLessThanAndVehicle_StatusOrderByReadingTimestampDesc(
            Double threshold, VehicleStatus status);
}
