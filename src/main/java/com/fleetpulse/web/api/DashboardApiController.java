package com.fleetpulse.web.api;

import com.fleetpulse.service.DashboardService;
import com.fleetpulse.web.dto.ApiResponse;
import com.fleetpulse.web.dto.DashboardSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard summary data")
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns KPI counts, chart data, recent alerts, and upcoming maintenance")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getSummary() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getSummary()));
    }
}
