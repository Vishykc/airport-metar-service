# Complete Instructions for Testing Airport METAR Service in Linux (WSL 2 Ubuntu)

## Prerequisites
Before testing, ensure you have:
1. WSL 2 with Ubuntu installed on Windows 11
2. Java 21+ installed
3. PostgreSQL 10+ installed and running
4. Maven installed
5. curl and psql command-line tools available

## 1. Database Setup Verification

First, verify your database is properly configured:

```bash
# Navigate to your project directory
cd /mnt/d/path/to/airport-metar-service

# Test database connection and permissions
./src/main/resources/test_database_setup.sh
```

If the script passes all tests, your database is correctly configured. If not, follow the error messages to fix any issues.

## 2. Running Unit Tests

Execute all unit tests to verify the application logic:

```bash
# Run all tests using Maven
./mvnw test
```

This will run:
- Controller tests (MetarControllerTest, SubscriptionControllerTest)
- Service tests (MetarServiceTest, SubscriptionServiceTest)

## 3. Running the Application

Start the Spring Boot application:

```bash
# Run the application directly
./mvnw spring-boot:run
```

Alternatively, you can build and run the JAR:

```bash
# Build the application
./mvnw clean package

# Run the JAR
java -jar target/airport-metar-service-0.0.1-SNAPSHOT.jar
```

## 4. Manual API Testing

With the application running, you can test the API endpoints manually using curl:

### 4.1 Subscription Management

```bash
# Subscribe to an airport (e.g., Zagreb - LDZA)
curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions

# Get all subscriptions
curl http://localhost:8080/subscriptions

# Unsubscribe from an airport
curl -X DELETE http://localhost:8080/subscriptions/LDZA
```

### 4.2 METAR Data Management

```bash
# Store METAR data for an airport
curl -X POST -H "Content-Type: application/json" -d '{"data": "METAR LDZA 030700Z 00000KT 9999 NSW SCT040 15/10 Q1013 NOSIG"}' http://localhost:8080/airport/LDZA/METAR

# Retrieve latest METAR data for an airport
curl http://localhost:8080/airport/LDZA/METAR
```

## 5. Automated Testing with Postman

Import the provided Postman collection for easier testing:

1. Open Postman
2. Click "Import" and select `src/main/resources/AirportMETARService.postman_collection.json`
3. Use the collection to test all endpoints with predefined requests

## 6. End-to-End Testing Workflow

Follow this complete workflow to test all components:

```bash
# 1. Start the application (in one terminal)
./mvnw spring-boot:run

# 2. Subscribe to an airport (in another terminal)
curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions

# 3. Verify subscription
curl http://localhost:8080/subscriptions

# 4. Run the automated METAR fetching script
chmod +x src/main/resources/fetch_metar_data.sh
./src/main/resources/fetch_metar_data.sh

# 5. Retrieve the stored METAR data
curl http://localhost:8080/airport/LDZA/METAR

# 6. Unsubscribe from the airport
curl -X DELETE http://localhost:8080/subscriptions/LDZA
```

## 7. Testing the Automated Script

The `fetch_metar_data.sh` script performs end-to-end testing of the entire system:

```bash
# Make the script executable
chmod +x src/main/resources/fetch_metar_data.sh

# Run the script with detailed logging
./src/main/resources/fetch_metar_data.sh
```

The script will:
1. Connect to the database to retrieve subscriptions
2. Fetch METAR data from NOAA for each subscribed airport
3. Send the data to your service via REST API
4. Log all operations with success/failure status

## 8. Health Checks

Verify the application is running properly:

```bash
# Check if the service is running
curl http://localhost:8080/actuator/health

# Get application info
curl http://localhost:8080/actuator/info

# Check available metrics
curl http://localhost:8080/actuator/metrics
```

## 9. Database Verification

You can also directly verify data in the database:

```bash
# Connect to the database
psql -U metar_user_dev -d metar_service_dev -h localhost -p 5432

# In the PostgreSQL prompt, run queries:
# SELECT * FROM subscriptions;
# SELECT * FROM metar ORDER BY id DESC LIMIT 10;
# \q to quit
```

## 10. Testing Error Cases

Test error handling by trying invalid operations:

```bash
# Try to subscribe with invalid ICAO code
curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "INVALID"}' http://localhost:8080/subscriptions

# Try to get METAR data for non-existent airport
curl http://localhost:8080/airport/NONEXISTENT/METAR

# Try to subscribe to the same airport twice
curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions
curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions

# Try to unsubscribe from non-subscribed airport
curl -X DELETE http://localhost:8080/subscriptions/NONEXISTENT
```

## 11. Performance Testing

For basic performance testing, you can use Apache Bench (install with `sudo apt-get install apache2-utils`):

```bash
# Test subscription endpoint performance
ab -n 100 -c 10 -p subscription_data.json -T "application/json" http://localhost:8080/subscriptions/

# Test METAR data retrieval performance
ab -n 100 -c 10 http://localhost:8080/airport/LDZA/METAR
```

## 12. Integration Testing

To perform complete integration testing:

1. Start the application
2. Subscribe to multiple airports:
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions
   curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDDU"}' http://localhost:8080/subscriptions
   curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "EHAM"}' http://localhost:8080/subscriptions
   ```
3. Run the fetch script:
   ```bash
   ./src/main/resources/fetch_metar_data.sh
   ```
4. Verify data for all airports:
   ```bash
   curl http://localhost:8080/airport/LDZA/METAR
   curl http://localhost:8080/airport/LDDU/METAR
   curl http://localhost:8080/airport/EHAM/METAR
   ```
5. Check all subscriptions:
   ```bash
   curl http://localhost:8080/subscriptions
   ```
6. Clean up by unsubscribing:
   ```bash
   curl -X DELETE http://localhost:8080/subscriptions/LDZA
   curl -X DELETE http://localhost:8080/subscriptions/LDDU
   curl -X DELETE http://localhost:8080/subscriptions/EHAM
   ```

These comprehensive testing instructions cover all aspects of the Airport METAR Service application, from unit tests to end-to-end integration testing.