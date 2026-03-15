package com.fleetpulse.web.api;

import com.fleetpulse.service.DriverService;
import com.fleetpulse.web.dto.ApiResponse;
import com.fleetpulse.web.dto.CreateDriverRequest;
import com.fleetpulse.web.dto.DriverDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Fleet driver management")
public class DriverApiController {

    private final DriverService driverService;

    @GetMapping
    @Operation(summary = "List all drivers")
    public ResponseEntity<ApiResponse<List<DriverDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(driverService.findAll()));
    }

    @GetMapping("/available")
    @Operation(summary = "List active drivers not currently assigned to any vehicle")
    public ResponseEntity<ApiResponse<List<DriverDto>>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.ok(driverService.findAvailable()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<ApiResponse<DriverDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(driverService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Register a new driver")
    public ResponseEntity<ApiResponse<DriverDto>> create(@Valid @RequestBody CreateDriverRequest request) {
        DriverDto created = driverService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a driver")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Driver removed", null));
    }
}
