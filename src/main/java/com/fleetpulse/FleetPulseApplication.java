package com.fleetpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FleetPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetPulseApplication.class, args);
    }
}
