package com.fleetpulse.domain.repository;

import com.fleetpulse.domain.enums.AlertSeverity;
import com.fleetpulse.domain.enums.AlertType;
import com.fleetpulse.domain.model.Alert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    List<Alert> findByResolvedFalseOrderByCreatedAtDesc(Pageable pageable);

    List<Alert> findByResolvedFalseOrderByCreatedAtDesc();

    long countByResolvedFalse();

    long countBySeverityAndResolvedFalse(AlertSeverity severity);

    @Query("SELECT a FROM Alert a WHERE a.vehicle.id = :vehicleId AND a.alertType = :alertType AND a.resolved = false")
    List<Alert> findActiveAlertsForVehicleAndType(Long vehicleId, AlertType alertType);
}
