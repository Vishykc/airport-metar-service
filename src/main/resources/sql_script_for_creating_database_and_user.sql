CREATE DATABASE metar_service;
CREATE USER metar_user WITH ENCRYPTED PASSWORD 'metar_password';
GRANT ALL PRIVILEGES ON DATABASE metar_service TO metar_user;