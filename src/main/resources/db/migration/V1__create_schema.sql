-- =========================================================
-- V1 : Create schema — FleetPulse
-- Compatible with H2 (default) and PostgreSQL (postgres profile)
-- =========================================================

CREATE TABLE drivers (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id      VARCHAR(50)  NOT NULL UNIQUE,
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    license_number   VARCHAR(50)  NOT NULL UNIQUE,
    license_expiry_date DATE      NOT NULL,
    contact_phone    VARCHAR(20),
    status           VARCHAR(20)  NOT NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vehicles (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_number   VARCHAR(50)  NOT NULL UNIQUE,
    make                  VARCHAR(100) NOT NULL,
    model                 VARCHAR(100) NOT NULL,
    manufacture_year       INT          NOT NULL,
    vehicle_type          VARCHAR(20)  NOT NULL,
    status                VARCHAR(20)  NOT NULL,
    fuel_type             VARCHAR(20)  NOT NULL,
    purchase_date         DATE,
    current_mileage       DOUBLE,
    next_service_mileage  DOUBLE,
    driver_id             BIGINT REFERENCES drivers(id) ON DELETE SET NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE maintenance_records (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id        BIGINT       NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    maintenance_type  VARCHAR(30)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    scheduled_date    DATE         NOT NULL,
    completed_date    DATE,
    mileage_at_service DOUBLE,
    cost_amount       DECIMAL(10,2),
    technician_name   VARCHAR(100),
    notes             VARCHAR(1000),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE telematics_readings (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id           BIGINT    NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    reading_timestamp    TIMESTAMP NOT NULL,
    mileage              DOUBLE    NOT NULL,
    fuel_level_percent   DOUBLE,
    engine_hours         DOUBLE,
    average_speed_kmh    DOUBLE,
    fuel_consumed_liters DOUBLE,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE alerts (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id  BIGINT       NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    alert_type  VARCHAR(30)  NOT NULL,
    severity    VARCHAR(20)  NOT NULL,
    message     VARCHAR(500) NOT NULL,
    is_resolved BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

-- Indexes for common query patterns
CREATE INDEX idx_vehicles_status     ON vehicles(status);
CREATE INDEX idx_maint_vehicle_id    ON maintenance_records(vehicle_id);
CREATE INDEX idx_maint_status        ON maintenance_records(status);
CREATE INDEX idx_maint_scheduled     ON maintenance_records(scheduled_date);
CREATE INDEX idx_alerts_vehicle_id   ON alerts(vehicle_id);
CREATE INDEX idx_alerts_resolved     ON alerts(is_resolved);
CREATE INDEX idx_telematics_vehicle  ON telematics_readings(vehicle_id);
