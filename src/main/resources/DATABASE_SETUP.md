-- ==========================================
-- Airport METAR Service: Database Setup Script
-- ==========================================

-- 1. Create the database
CREATE DATABASE metar_service_dev;

-- 2. Create the user with password
CREATE USER metar_user_dev WITH ENCRYPTED PASSWORD 'metar_password_dev';

-- 3. Grant privileges on the database
GRANT ALL PRIVILEGES ON DATABASE metar_service_dev TO metar_user_dev;

-- 4. Connect to the database
\connect metar_service_dev;

-- 5. Grant privileges on the public schema
GRANT ALL ON SCHEMA public TO metar_user_dev;

-- 6. Grant privileges on existing tables and sequences
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO metar_user_dev;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO metar_user_dev;

-- 7. Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO metar_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO metar_user_dev;

-- ==========================================
-- Optional: Verify user permissions
-- ==========================================
-- Check current user
SELECT current_user;

-- Check current database
SELECT current_database();

-- Check schema privileges (use this instead of non-existent schema_privileges)
SELECT grantee, privilege_type
FROM information_schema.role_schema_grants
WHERE grantee = 'metar_user_dev' AND schema_name = 'public';

-- Or, simpler, check with function
SELECT has_schema_privilege('metar_user_dev', 'public', 'CREATE') AS can_create;