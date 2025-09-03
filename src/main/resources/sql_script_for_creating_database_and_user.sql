CREATE DATABASE metar_service_dev;
CREATE USER metar_user_dev WITH ENCRYPTED PASSWORD 'metar_password_dev';
GRANT ALL PRIVILEGES ON DATABASE metar_service_dev TO metar_user_dev;

-- Connect to the metar_service database and grant schema permissions
\connect metar_service_dev;
GRANT ALL ON SCHEMA public TO metar_user_dev;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO metar_user_dev;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO metar_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO metar_user_dev;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO metar_user_dev;