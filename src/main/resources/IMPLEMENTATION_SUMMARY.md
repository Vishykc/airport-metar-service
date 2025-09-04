# Airport METAR Service Implementation Summary

This document summarizes the implementation of the Airport METAR Service according to the BE Developer Assignment requirements.

## 1. The Service

### 1.1 Subscription Endpoints

All required subscription endpoints have been implemented:

- **POST /subscriptions** - Subscribe to an airport
  - Example: `POST /subscriptions` with JSON payload `{"icaoCode": "LDZA"}` subscribes the Zagreb airport
  - Returns HTTP 201 (Created) on success
  - Returns HTTP 409 (Conflict) if already subscribed

- **GET /subscriptions** - List all subscribed airports
  - Returns a JSON array of subscriptions
  - Example: `[{"id": 1, "icaoCode": "LDZA", "active": true}]`

- **DELETE /subscriptions/{icaoCode}** - Unsubscribe from an airport
  - Example: `DELETE /subscriptions/LDZA` unsubscribes the Zagreb airport
  - Returns HTTP 204 (No Content) on success
  - Returns HTTP 404 (Not Found) if not subscribed

### 1.2 METAR Data Endpoints

All required METAR data endpoints have been implemented:

- **POST /airport/{icaoCode}/METAR** - Store METAR data for an airport
  - Example: `POST /airport/LDZA/METAR` with JSON payload `{"data": "METAR LDZA 121200Z..."}` stores the data
  - Returns HTTP 201 (Created) with the stored data

- **GET /airport/{icaoCode}/METAR** - Retrieve the latest METAR data for an airport
  - Returns HTTP 200 (OK) with the latest METAR data
  - Returns HTTP 404 (Not Found) if no data exists

## 2. Automated Task

A Linux bash script (`fetch_metar_data.sh`) has been created that:

1. Connects to the PostgreSQL database to retrieve all active airport subscriptions
2. For each subscribed airport:
   - Fetches raw METAR data from the NOAA service (https://tgftp.nws.noaa.gov/data/observations/metar/stations/)
   - Extracts the METAR line from the response
   - Sends the raw METAR data to the Airport METAR service via REST API
3. Provides detailed logging of all operations with success/failure counts
4. Handles errors gracefully and continues processing other airports even if one fails

The script can be scheduled with cron for regular execution.

## 3. The Database

The database contains the required tables:

### 3.1 Subscriptions Table
- Stores all subscribed airports
- Columns: id (auto-generated), icao_code (unique), active (boolean)

### 3.2 METAR Table
- Stores METAR data for airports
- Columns: id (auto-generated), icao_code, raw_data

## 4. Technology Stack

- **Java 8+**: Implemented with Java 21
- **Spring Framework**: Using Spring Boot 3.5.5
- **RDBMS**: PostgreSQL with JPA/Hibernate
- **Linux**: The automated task script is designed for Linux execution

## 5. Extra Tasks Status

The following extra tasks have been identified but not implemented as per requirements:

- [ ] Extend subscription endpoint to support activation/deactivation with PUT method
- [ ] Provide filtering capabilities when returning airport subscriptions
- [ ] Parse and split METAR data, storing elements in separate fields
- [ ] Extend METAR retrieval endpoint to allow retrieving only a subset of data
- [ ] Decode METAR data into natural language

## 6. Files Created

1. `src/main/resources/fetch_metar_data.sh` - Automated task script
2. `src/main/resources/SCRIPT_README.md` - Documentation for the script
3. `src/main/resources/sql_script_for_creating_database_and_user.sql` - Database setup script
4. `src/main/resources/DATABASE_SETUP.md` - Detailed database setup instructions
5. `src/main/resources/AirportMETARService.postman_collection.json` - Postman collection for API testing

## 7. Testing

The service can be tested manually by:

1. Running the Spring Boot application
2. Subscribing to an airport using the API
3. Running the fetch_metar_data.sh script
4. Retrieving the stored METAR data through the API
5. Deactivating a subscription using the PUT endpoint:
   - Example: `PUT /subscriptions/LDZA` with JSON payload `{"active": "0"}` deactivates the Zagreb airport subscription
6. Activating a subscription using the PUT endpoint:
   - Example: `PUT /subscriptions/LDZA` with JSON payload `{"active": "1"}` activates the Zagreb airport subscription
7. Alternatively, you can use the provided test script `TEST_PUT_ENDPOINT.sh` which demonstrates all the PUT endpoint functionality

All required components have been implemented according to the assignment specifications.