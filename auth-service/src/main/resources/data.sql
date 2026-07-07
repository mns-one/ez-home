DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                  UUID            PRIMARY KEY,
  email               VARCHAR(255)    NOT NULL UNIQUE,
  passwordhash        VARCHAR(255),
  role                VARCHAR(255)
);