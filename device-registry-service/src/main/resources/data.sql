-- Ensure table exists in db
CREATE TABLE IF NOT EXISTS registry (
  id                    UUID            PRIMARY KEY,
  serial_no             VARCHAR(255)    NOT NULL UNIQUE,
  batch_no              VARCHAR(255)    NOT NULL,
  model_name            VARCHAR(255)    NOT NULL,
  factory_location      VARCHAR(255)    NOT NULL,
  created_at            TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert dummy device records for testing
INSERT INTO "registry" (id, serial_no, batch_no, model_name, factory_location)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'SMT5001', 'BMC101', '4gang', 'Mumbai'
WHERE NOT EXISTS (
    SELECT 1
    FROM "registry"
    WHERE id = '223e4567-e89b-12d3-a456-426614174006'
);

INSERT INTO "registry" (id, serial_no, batch_no, model_name, factory_location)
SELECT '223e4567-e89b-12d3-a456-426614174008', 'SMT5002', 'BMC101', '4gang', 'NCR'
WHERE NOT EXISTS (
    SELECT 1
    FROM "registry"
    WHERE id = '223e4567-e89b-12d3-a456-426614174008'
);

INSERT INTO "registry" (id, serial_no, batch_no, model_name, factory_location)
SELECT '223e4567-e89b-12d3-a456-426614174004', 'SMT5003', 'BMC102', '2gang', 'Banglore'
WHERE NOT EXISTS (
    SELECT 1
    FROM "registry"
    WHERE id = '223e4567-e89b-12d3-a456-426614174004'
);

INSERT INTO "registry" (id, serial_no, batch_no, model_name, factory_location)
SELECT '223e4567-e89b-12d3-a456-426614174003', 'SMT5004', 'BMC102', 'FanSwitch', 'Banglore'
WHERE NOT EXISTS (
    SELECT 1
    FROM "registry"
    WHERE id = '223e4567-e89b-12d3-a456-426614174003'
);

INSERT INTO "registry" (id, serial_no, batch_no, model_name, factory_location)
SELECT '223e4567-e89b-12d3-a456-426614174001', 'SMT5005', 'BMC102', '2gang-white', 'UP'
WHERE NOT EXISTS (
    SELECT 1
    FROM "registry"
    WHERE id = '223e4567-e89b-12d3-a456-426614174001'
);