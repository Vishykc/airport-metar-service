CREATE DATABASE metar_service;
CREATE USER metar_user WITH ENCRYPTED PASSWORD 'metar_password';
GRANT ALL PRIVILEGES ON DATABASE metar_service TO metar_user;

-- Connect to the metar_service database and grant schema permissions
\connect metar_service;
GRANT ALL ON SCHEMA public TO metar_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO metar_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO metar_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO metar_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO metar_user;