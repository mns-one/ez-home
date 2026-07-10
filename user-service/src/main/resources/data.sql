-- Ensure table exists in db
CREATE TABLE IF NOT EXISTS users (
  id                    UUID            PRIMARY KEY,
  email                 VARCHAR(255)    NOT NULL UNIQUE,
  username              VARCHAR(255),
  city                  VARCHAR(255),
  profile_image_url     VARCHAR(255),
  created_at            TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert dummy user accounts for testing
INSERT INTO "users" (id, email, username, city, profile_image_url)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'user@test.com', 'user', 'city1', 'xyz.com'
WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174006'
       OR email = 'user@test.com'
);

INSERT INTO "users" (id, email, username, city, profile_image_url)
SELECT '223e4567-e89b-12d3-a456-426614174008', 'admin@test.com', 'user', 'city1', 'xyz.com'
WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174008'
       OR email = 'admin@test.com'
);
