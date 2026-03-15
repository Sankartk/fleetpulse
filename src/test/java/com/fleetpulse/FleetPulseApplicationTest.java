package com.fleetpulse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FleetPulseApplicationTest {

    @Test
    void contextLoads() {
        // Verifies the full Spring context starts successfully with test profile
    }
}
