package com.fleetpulse.web.view;

import com.fleetpulse.service.DashboardService;
import com.fleetpulse.service.VehicleService;
import com.fleetpulse.web.dto.DashboardSummaryDto;
import com.fleetpulse.web.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardViewController {

    private final DashboardService dashboardService;
    private final VehicleService vehicleService;

    @GetMapping("/")
    public String dashboard(Model model) {
        DashboardSummaryDto summary = dashboardService.getSummary();
        model.addAttribute("summary", summary);

        // Serialize chart data for Chart.js (Thymeleaf inline JS)
        model.addAttribute("monthlyCostLabels", summary.monthlyCostLabels());
        model.addAttribute("monthlyCostValues", summary.monthlyCostValues());

        return "dashboard";
    }

    @GetMapping("/vehicles")
    public String vehicles(Model model) {
        List<VehicleDto> vehicles = vehicleService.findAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicles";
    }
}
