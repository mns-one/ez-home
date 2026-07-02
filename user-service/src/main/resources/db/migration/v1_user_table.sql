DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_sequence;

CREATE SEQUENCE IF NOT EXISTS user_sequence
    START WITH 1000
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS users (
  userId        BIGINT          NOT NULL DEFAULT nextval('user_sequence'),
  username      VARCHAR(255)    NOT NULL,
  email         VARCHAR(255)    NOT NULL UNIQUE,
  createdAt     DATE            NOT NULL DEFAULT CURRENT_TIMESTAMP,
);
