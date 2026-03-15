package com.fleetpulse.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.enums.VehicleType;
import com.fleetpulse.exception.GlobalExceptionHandler;
import com.fleetpulse.exception.ResourceNotFoundException;
import com.fleetpulse.service.VehicleService;
import com.fleetpulse.web.api.VehicleApiController;
import com.fleetpulse.web.dto.CreateVehicleRequest;
import com.fleetpulse.web.dto.VehicleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleApiController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("VehicleApiController integration tests")
class VehicleApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  VehicleService vehicleService;

    private VehicleDto sampleDto() {
        return new VehicleDto(1L, "FP-TRK-001", "Ford", "F-350", 2021,
                VehicleType.TRUCK, VehicleStatus.ACTIVE, FuelType.DIESEL,
                null, 48000.0, 50000.0, null, null,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/vehicles returns 200 with vehicle list")
    void getAll_returns200() throws Exception {
        when(vehicleService.findAll()).thenReturn(List.of(sampleDto()));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].registrationNumber").value("FP-TRK-001"))
                .andExpect(jsonPath("$.data[0].make").value("Ford"));
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} returns 404 when not found")
    void getById_notFound_returns404() throws Exception {
        when(vehicleService.findById(99L)).thenThrow(new ResourceNotFoundException("Vehicle", 99L));

        mockMvc.perform(get("/api/vehicles/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/vehicles returns 201 with created vehicle")
    void create_validRequest_returns201() throws Exception {
        CreateVehicleRequest request = new CreateVehicleRequest(
                "FP-NEW-009", "Toyota", "Hilux", 2023,
                VehicleType.PICKUP, FuelType.DIESEL, null, 0.0, 5000.0, null);

        VehicleDto created = new VehicleDto(42L, "FP-NEW-009", "Toyota", "Hilux", 2023,
                VehicleType.PICKUP, VehicleStatus.ACTIVE, FuelType.DIESEL,
                null, 0.0, 5000.0, null, null, LocalDateTime.now(), LocalDateTime.now());
        when(vehicleService.create(any())).thenReturn(created);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(42))
                .andExpect(jsonPath("$.data.registrationNumber").value("FP-NEW-009"));
    }

    @Test
    @DisplayName("POST /api/vehicles returns 400 for missing required fields")
    void create_missingFields_returns400() throws Exception {
        // Missing make and model
        String body = """
                {
                    "registrationNumber": "FP-BAD-999",
                    "year": 2023,
                    "vehicleType": "TRUCK",
                    "fuelType": "DIESEL"
                }
                """;

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
