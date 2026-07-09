DROP TABLE IF EXISTS devices;

CREATE TABLE devices (
  id                    UUID            PRIMARY KEY,
  serial_no             VARCHAR(255)    NOT NULL UNIQUE,
  user_id               UUID            NOT NULL,
  model_name            VARCHAR(255)    NOT NULL,
  custom_name           VARCHAR(255),
  alerts_enabled        BOOLEAN         NOT NULL DEFAULT FALSE,
  created_at            TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO "devices" (id, serial_no, user_id, model_name, custom_name)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'SMT5001', '223e4567-e89b-12d3-a456-426614174006', '4gang', 'door';

INSERT INTO "devices" (id, serial_no, user_id, model_name, custom_name)
SELECT '223e4567-e89b-12d3-a456-426614174007', 'SMT5002', '223e4567-e89b-12d3-a456-426614174006', '4gang', 'door';

INSERT INTO "devices" (id, serial_no, user_id, model_name, custom_name)
SELECT '223e4567-e89b-12d3-a456-426614174008', 'SMT5003', '223e4567-e89b-12d3-a456-426614174006', '4gang', 'door';
