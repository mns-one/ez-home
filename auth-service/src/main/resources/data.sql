DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                  UUID            PRIMARY KEY,
  email               VARCHAR(255)    NOT NULL UNIQUE,
  passwordhash        VARCHAR(255),
  role                VARCHAR(255)
);

INSERT INTO "users" (id, email, passwordhash, role)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'user@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'USER'
WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174006'
       OR email = 'user@test.com'
);

INSERT INTO "users" (id, email, passwordhash, role)
SELECT '223e4567-e89b-12d3-a456-426614174008', 'admin@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174008'
       OR email = 'admin@test.com'
);
