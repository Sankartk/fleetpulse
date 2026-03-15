package com.fleetpulse.web.api;

import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.service.VehicleService;
import com.fleetpulse.web.dto.ApiResponse;
import com.fleetpulse.web.dto.CreateVehicleRequest;
import com.fleetpulse.web.dto.UpdateVehicleRequest;
import com.fleetpulse.web.dto.VehicleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Fleet vehicle management")
public class VehicleApiController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "List all vehicles")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> getAll(
            @RequestParam(required = false) VehicleStatus status) {
        List<VehicleDto> result = (status != null)
                ? vehicleService.findByStatus(status)
                : vehicleService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<ApiResponse<VehicleDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(vehicleService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Register a new vehicle")
    public ResponseEntity<ApiResponse<VehicleDto>> create(@Valid @RequestBody CreateVehicleRequest request) {
        VehicleDto created = vehicleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle details")
    public ResponseEntity<ApiResponse<VehicleDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(vehicleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle deleted", null));
    }

    @PatchMapping("/{vehicleId}/assign-driver/{driverId}")
    @Operation(summary = "Assign a driver to a vehicle")
    public ResponseEntity<ApiResponse<VehicleDto>> assignDriver(
            @PathVariable Long vehicleId,
            @PathVariable Long driverId) {
        return ResponseEntity.ok(ApiResponse.ok(vehicleService.assignDriver(vehicleId, driverId)));
    }

    @PatchMapping("/{vehicleId}/unassign-driver")
    @Operation(summary = "Remove driver assignment from a vehicle")
    public ResponseEntity<ApiResponse<VehicleDto>> unassignDriver(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(ApiResponse.ok(vehicleService.unassignDriver(vehicleId)));
    }
}
