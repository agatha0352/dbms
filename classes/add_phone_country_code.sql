-- Add phone_country_code column to users table
ALTER TABLE users ADD COLUMN phone_country_code VARCHAR(8);

-- Update user inserts to include phone_country_code
-- Example: ('admin', ..., '+1', ...)
-- You will need to update your application entity/model as well to support this new column.
