ALTER TABLE IF EXISTS daily_usage
ALTER COLUMN id SET DEFAULT gen_random_uuid();
