package com.fleetpulse.domain.repository;

import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.model.MaintenanceRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {

    List<MaintenanceRecord> findByVehicleIdOrderByScheduledDateDesc(Long vehicleId);

    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);

    long countByStatus(MaintenanceStatus status);

    List<MaintenanceRecord> findByScheduledDateBetweenOrderByScheduledDate(LocalDate start, LocalDate end);

    @Query("""
            SELECT m FROM MaintenanceRecord m JOIN FETCH m.vehicle
            WHERE m.status = 'SCHEDULED' AND m.scheduledDate < :today
            """)
    List<MaintenanceRecord> findOverdueRecords(@Param("today") LocalDate today);

    @Query("""
            SELECT m FROM MaintenanceRecord m JOIN FETCH m.vehicle
            WHERE m.status IN ('SCHEDULED', 'IN_PROGRESS')
              AND m.scheduledDate BETWEEN :start AND :end
            ORDER BY m.scheduledDate
            """)
    List<MaintenanceRecord> findUpcoming(@Param("start") LocalDate start, @Param("end") LocalDate end);

    List<MaintenanceRecord> findByStatusAndCompletedDateAfter(MaintenanceStatus status, LocalDate since);

    @Query("""
            SELECT m FROM MaintenanceRecord m JOIN FETCH m.vehicle
            ORDER BY m.createdAt DESC
            """)
    List<MaintenanceRecord> findRecentWithVehicles(Pageable pageable);
}
