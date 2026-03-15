package com.fleetpulse.domain.repository;

import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(VehicleStatus status);

    long countByStatus(VehicleStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.assignedDriver WHERE v.id = :id")
    Optional<Vehicle> findByIdWithDriver(Long id);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.assignedDriver ORDER BY v.registrationNumber")
    List<Vehicle> findAllWithDrivers();
}
