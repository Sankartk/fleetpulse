package com.fleetpulse.domain.repository;

import com.fleetpulse.domain.enums.DriverStatus;
import com.fleetpulse.domain.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmployeeId(String employeeId);

    List<Driver> findByStatus(DriverStatus status);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByEmployeeId(String employeeId);

    @Query("""
            SELECT d FROM Driver d
            WHERE d.status = 'ACTIVE'
              AND NOT EXISTS (
                  SELECT v FROM Vehicle v
                  WHERE v.assignedDriver = d
                    AND v.status = 'ACTIVE'
              )
            """)
    List<Driver> findAvailableDrivers();
}
