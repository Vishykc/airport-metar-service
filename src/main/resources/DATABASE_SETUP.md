# Database Setup Instructions

This document provides detailed instructions for setting up the PostgreSQL database for the Airport METAR Service.

## Prerequisites

1. PostgreSQL 10+ installed and running
2. Administrative access to PostgreSQL (usually postgres user)

## Setup Steps

### 1. Connect to PostgreSQL as Administrator

```bash
# Using psql as postgres user (default PostgreSQL admin)
sudo -u postgres psql
```

Or if you have postgres user password:
```bash
psql -U postgres -h localhost -p 5432
```

### 2. Execute the Database Creation Script

Run the provided SQL script to create the database and user with proper permissions:

```sql
-- Execute the script from psql
\i /path/to/sql_script_for_creating_database_and_user.sql
```

Alternatively, copy and paste the contents of the script directly into the psql prompt:

```sql
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
-- 8. Verify permissions using has_schema_privilege()
-- ==========================================

-- Check if user can CREATE tables in public schema
SELECT 'CREATE' AS privilege, has_schema_privilege('metar_user_dev', 'public', 'CREATE') AS allowed;

-- Check if user can USAGE the schema
SELECT 'USAGE' AS privilege, has_schema_privilege('metar_user_dev', 'public', 'USAGE') AS allowed;

-- doesn't exist
-- Optional: verify table-level privileges (if tables exist)
SELECT grantee, privilege_type, table_name
FROM information_schema.table_privileges
WHERE grantee = 'metar_user_dev';

```

### 3. Verify the Setup

You can verify that the user has the correct permissions by connecting as the metar_user:

```bash
psql -U metar_user_dev -d metar_service_dev -h localhost -p 5432
```

Then check permissions:
```sql
-- Check current user
SELECT current_user;

-- Check current database
SELECT current_database();

-- Check schema permissions (should show permissions for metar_user)
SELECT grantee, privilege_type 
FROM information_schema.schema_privileges 
WHERE schema_name = 'public';
```

### 4. Start the Application

After setting up the database, you can start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

Or build and run the JAR:
```bash
./mvnw clean package
java -jar target/airport-metar-service-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Permission Denied Error

If you still encounter "permission denied for schema public" errors:

1. Ensure you've connected to the correct database when granting schema permissions
2. Check that PostgreSQL is configured to allow connections from localhost
3. Verify the user and password in application.properties match those created in the database

### Connection Refused

If you get connection refused errors:

1. Check that PostgreSQL is running:
   ```bash
   sudo systemctl status postgresql
   ```
2. Verify the connection details in application.properties
3. Check PostgreSQL configuration files (postgresql.conf, pg_hba.conf) for network settings

### Manual Permission Fix

If you need to manually grant permissions after database creation:

```sql
-- Connect as postgres user
sudo -u postgres psql

-- Connect to metar_service database
\connect metar_service;

-- Grant necessary permissions
GRANT ALL ON SCHEMA public TO metar_user_dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO metar_user_dev;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO metar_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO metar_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO metar_user_dev;