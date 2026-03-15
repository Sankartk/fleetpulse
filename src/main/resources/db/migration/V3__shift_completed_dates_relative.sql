-- V3: Shift completed_date values to be relative to today so the cost trend chart shows real data.
--     V2 hardcoded dates in 2024 which fall outside the rolling 6-month chart window.
--
--     Result:  Oct → $280.50  |  Nov → $1,240  |  Dec → $620  |  Jan → $150  |  Feb → $210  |  Mar → $155

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -5, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -5, CURRENT_DATE)
WHERE id = 1;   -- OIL_CHANGE    FP-TRK-001  $185.50

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -5, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -5, CURRENT_DATE)
WHERE id = 2;   -- TIRE_ROTATION  FP-TRK-001  $95.00

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -3, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -3, CURRENT_DATE)
WHERE id = 4;   -- BRAKE_SERVICE  FP-VAN-002  $620.00

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -2, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -2, CURRENT_DATE)
WHERE id = 5;   -- INSPECTION     FP-VAN-002  $150.00

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -4, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -4, CURRENT_DATE)
WHERE id = 6;   -- ENGINE_SERVICE FP-SUV-003  $1240.00

UPDATE maintenance_records SET
    scheduled_date = DATEADD('MONTH', -1, CURRENT_DATE),
    completed_date = DATEADD('MONTH', -1, CURRENT_DATE)
WHERE id = 10;  -- OIL_CHANGE     FP-SED-005  $210.00

UPDATE maintenance_records SET
    scheduled_date = DATEADD('DAY', -10, CURRENT_DATE),
    completed_date = DATEADD('DAY', -10, CURRENT_DATE)
WHERE id = 12;  -- INSPECTION     FP-SED-008  $155.00
