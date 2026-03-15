package com.fleetpulse.web.api;

import com.fleetpulse.service.MaintenanceService;
import com.fleetpulse.web.dto.ApiResponse;
import com.fleetpulse.web.dto.CreateMaintenanceRequest;
import com.fleetpulse.web.dto.MaintenanceRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@Tag(name = "Maintenance", description = "Vehicle maintenance scheduling")
public class MaintenanceApiController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "List all maintenance records")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get maintenance record by ID")
    public ResponseEntity<ApiResponse<MaintenanceRecordDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.findById(id)));
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get all maintenance records for a vehicle")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDto>>> getByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.findByVehicleId(vehicleId)));
    }

    @GetMapping("/overdue")
    @Operation(summary = "List all overdue maintenance records")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDto>>> getOverdue() {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.findOverdue()));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "List maintenance scheduled in the next 30 days")
    public ResponseEntity<ApiResponse<List<MaintenanceRecordDto>>> getUpcoming() {
        return ResponseEntity.ok(ApiResponse.ok(maintenanceService.findUpcomingThirtyDays()));
    }

    @PostMapping
    @Operation(summary = "Schedule a new maintenance task")
    public ResponseEntity<ApiResponse<MaintenanceRecordDto>> schedule(
            @Valid @RequestBody CreateMaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(maintenanceService.schedule(request)));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark a maintenance record as completed")
    public ResponseEntity<ApiResponse<MaintenanceRecordDto>> complete(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal actualCost,
            @RequestParam(required = false) String technicianName) {
        return ResponseEntity.ok(ApiResponse.ok(
                maintenanceService.complete(id, actualCost, technicianName)));
    }
}
