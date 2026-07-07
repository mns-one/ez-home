DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                    UUID            PRIMARY KEY,
  email                 VARCHAR(255)    NOT NULL UNIQUE,
  username              VARCHAR(255),
  city                  VARCHAR(255),
  profile_image_url     VARCHAR(255),
  created_at            TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

