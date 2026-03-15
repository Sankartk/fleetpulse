package com.fleetpulse.web.api;

import com.fleetpulse.service.AlertService;
import com.fleetpulse.web.dto.AlertDto;
import com.fleetpulse.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Fleet operational alerts")
public class AlertApiController {

    private final AlertService alertService;

    @GetMapping
    @Operation(summary = "List all alerts")
    public ResponseEntity<ApiResponse<List<AlertDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(alertService.findAll()));
    }

    @GetMapping("/unresolved")
    @Operation(summary = "List all unresolved alerts")
    public ResponseEntity<ApiResponse<List<AlertDto>>> getUnresolved() {
        return ResponseEntity.ok(ApiResponse.ok(alertService.findUnresolved()));
    }

    @GetMapping("/count")
    @Operation(summary = "Count unresolved alerts")
    public ResponseEntity<ApiResponse<Long>> countUnresolved() {
        return ResponseEntity.ok(ApiResponse.ok(alertService.countUnresolved()));
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Resolve an alert")
    public ResponseEntity<ApiResponse<AlertDto>> resolve(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Alert resolved", alertService.resolve(id)));
    }
}
