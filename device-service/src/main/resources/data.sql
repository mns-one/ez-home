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