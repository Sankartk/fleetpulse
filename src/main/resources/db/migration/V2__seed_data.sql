-- =========================================================
-- V2 : Seed data — realistic fleet scenario
-- =========================================================

-- Drivers
INSERT INTO drivers (id, employee_id, first_name, last_name, license_number, license_expiry_date, contact_phone, status) VALUES
(1,  'EMP001', 'Marcus',   'Johnson',  'DL-2021-001', '2026-08-15', '+1-555-0101', 'ACTIVE'),
(2,  'EMP002', 'Sarah',    'Chen',     'DL-2020-042', '2026-11-30', '+1-555-0102', 'ACTIVE'),
(3,  'EMP003', 'David',    'Ramirez',  'DL-2019-117', '2027-03-22', '+1-555-0103', 'ACTIVE'),
(4,  'EMP004', 'Lisa',     'Thompson', 'DL-2022-308', '2026-01-10', '+1-555-0104', 'ON_LEAVE'),
(5,  'EMP005', 'James',    'Wilson',   'DL-2018-205', '2027-12-05', '+1-555-0105', 'ACTIVE');

-- Vehicles
INSERT INTO vehicles (id, registration_number, make, model, manufacture_year, vehicle_type, status, fuel_type, purchase_date, current_mileage, next_service_mileage, driver_id) VALUES
(1, 'FP-TRK-001', 'Ford',       'F-350',       2021, 'TRUCK', 'ACTIVE',         'DIESEL',  '2021-03-15', 48230.5,  50000.0, 1),
(2, 'FP-VAN-002', 'Mercedes',   'Sprinter',    2022, 'VAN',   'ACTIVE',         'DIESEL',  '2022-07-01', 32100.0,  35000.0, 2),
(3, 'FP-SUV-003', 'Toyota',     'Land Cruiser',2020, 'SUV',   'ACTIVE',         'PETROL',  '2020-11-20', 61500.0,  65000.0, 3),
(4, 'FP-TRK-004', 'Volvo',      'FH16',        2019, 'TRUCK', 'IN_MAINTENANCE', 'DIESEL',  '2019-05-10', 125400.0, 130000.0,NULL),
(5, 'FP-SED-005', 'BMW',        '530d',        2023, 'SEDAN', 'ACTIVE',         'DIESEL',  '2023-01-08', 18900.0,  20000.0, 5),
(6, 'FP-VAN-006', 'Renault',    'Master',      2021, 'VAN',   'ACTIVE',         'DIESEL',  '2021-09-14', 41200.0,  45000.0, NULL),
(7, 'FP-TRK-007', 'MAN',        'TGX',         2018, 'TRUCK', 'RETIRED',        'DIESEL',  '2018-02-28', 198700.0, NULL,    NULL),
(8, 'FP-SUV-008', 'Land Rover', 'Defender',    2022, 'SUV',   'ACTIVE',         'HYBRID',  '2022-04-05', 27600.0,  30000.0, 4);

-- Maintenance Records
INSERT INTO maintenance_records (id, vehicle_id, maintenance_type, status, scheduled_date, completed_date, mileage_at_service, cost_amount, technician_name, notes) VALUES
(1,  1, 'OIL_CHANGE',    'COMPLETED',    '2024-10-15', '2024-10-16', 45000.0, 185.50,  'Mike Torres',   'Synthetic 5W-30 oil change'),
(2,  1, 'TIRE_ROTATION', 'COMPLETED',    '2024-11-01', '2024-11-01', 46500.0, 95.00,   'Mike Torres',   'All 4 tires rotated and balanced'),
(3,  1, 'OIL_CHANGE',    'SCHEDULED',    '2025-06-15', NULL,         NULL,    NULL,    NULL,            'Scheduled 50k mile service'),
(4,  2, 'BRAKE_SERVICE', 'COMPLETED',    '2024-09-20', '2024-09-22', 30000.0, 620.00,  'Anna Kovacs',   'Front brake pads and rotors replaced'),
(5,  2, 'INSPECTION',    'COMPLETED',    '2024-12-01', '2024-12-01', 32000.0, 150.00,  'Anna Kovacs',   'Annual inspection — passed with comments'),
(6,  3, 'ENGINE_SERVICE','COMPLETED',    '2024-08-10', '2024-08-14', 58000.0, 1240.00, 'Roberto Lima',  'Full engine service at 60k interval'),
(7,  3, 'TIRE_ROTATION', 'OVERDUE',      '2025-01-15', NULL,         NULL,    NULL,    NULL,            'Overdue — schedule immediately'),
(8,  4, 'BRAKE_SERVICE', 'IN_PROGRESS',  '2025-02-01', NULL,        125000.0, NULL,   'Derek Hoang',   'Vehicle in maintenance bay — parts on order'),
(9,  4, 'ENGINE_SERVICE','SCHEDULED',    '2025-07-15', NULL,         NULL,    2100.00, 'Derek Hoang',   'Major engine overhaul due at 130k miles'),
(10, 5, 'OIL_CHANGE',    'COMPLETED',    '2024-11-20', '2024-11-20', 18000.0, 210.00,  'Anna Kovacs',   'BMW-approved full synthetic oil service'),
(11, 6, 'OIL_CHANGE',    'SCHEDULED',    '2025-07-01', NULL,         NULL,    175.00,  NULL,            'Routine oil change at 45k miles'),
(12, 8, 'INSPECTION',    'COMPLETED',    '2025-01-10', '2025-01-10', 27000.0, 155.00,  'Mike Torres',   'Annual inspection — passed');

-- Telematics Readings
INSERT INTO telematics_readings (vehicle_id, reading_timestamp, mileage, fuel_level_percent, engine_hours, average_speed_kmh, fuel_consumed_liters) VALUES
(1, '2025-03-01 08:00:00', 48230.5, 72.0, 1205.5, 65.3, 32.1),
(1, '2025-03-05 14:30:00', 48430.5, 52.0, 1210.8, 71.2, 28.4),
(2, '2025-03-02 09:15:00', 32100.0, 88.0, 820.3,  58.7, 24.6),
(3, '2025-03-03 11:00:00', 61500.0, 45.0, 1540.2, 75.1, 38.9),
(3, '2025-03-06 16:45:00', 61700.0, 21.0, 1544.5, 68.3, 35.2),
(5, '2025-03-04 08:30:00', 18900.0, 91.0, 473.6,  82.4, 22.1),
(6, '2025-03-01 10:00:00', 41200.0, 63.0, 1030.1, 61.5, 26.8),
(8, '2025-03-05 09:45:00', 27600.0, 78.0, 689.4,  70.8, 19.3);

-- Alerts
INSERT INTO alerts (vehicle_id, alert_type, severity, message, is_resolved, created_at) VALUES
(3, 'FUEL_LOW',          'HIGH',     'Vehicle FP-SUV-003: Fuel level critically low at 21%. Refuel required immediately.',              FALSE, '2025-03-06 17:00:00'),
(1, 'MILEAGE_THRESHOLD', 'MEDIUM',   'Vehicle FP-TRK-001: Approaching scheduled service milestone at 50,000 miles (current: 48,230).', FALSE, '2025-03-05 15:00:00'),
(3, 'MAINTENANCE_DUE',   'HIGH',     'Vehicle FP-SUV-003: Tire rotation scheduled for 2025-01-15 is OVERDUE by 54 days.',              FALSE, '2025-03-10 09:00:00'),
(2, 'LICENSE_EXPIRY',    'MEDIUM',   'Driver Sarah Chen (EMP002): License DL-2020-042 expires on 2026-11-30. Plan renewal.',           FALSE, '2025-03-01 08:00:00'),
(4, 'MAINTENANCE_DUE',   'CRITICAL', 'Vehicle FP-TRK-004: Currently in maintenance bay. Brake service in progress.',                   FALSE, '2025-02-01 09:00:00'),
(5, 'MILEAGE_THRESHOLD', 'LOW',      'Vehicle FP-SED-005: Next service due at 20,000 miles (current: 18,900). 1,100 miles remaining.', TRUE,  '2025-02-15 10:00:00');
