# Postman Collection Instructions

This document provides instructions for importing and using the Postman collection for testing the Airport METAR Service API.

## Prerequisites

1. [Postman](https://www.postman.com/downloads/) installed on your system
2. The Airport METAR Service application running locally

## Importing the Collection

1. Open Postman
2. Click on the "Import" button in the top left corner
3. Select "Upload Files"
4. Navigate to and select the `AirportMETARService.postman_collection.json` file from this project
5. Click "Open" to import the collection

## Using the Collection

### Environment Setup

The collection uses a variable `{{base_url}}` which defaults to `http://localhost:8080`. If your service is running on a different host or port, you can update this variable:

1. Click on the "Environment" quick look icon (eye icon) in the top right
2. Click "Edit" next to the "No Environment" dropdown
3. Add a new variable:
   - Name: `base_url`
   - Value: Your service URL (e.g., `http://localhost:8080`)
4. Click "Update"

### Available Endpoints

The collection includes the following request folders:

#### Subscriptions
1. **Subscribe to Airport** - POST `/subscriptions`
   - Adds a new airport subscription
   - Request body example:
     ```json
     {
       "icaoCode": "LDZA"
     }
     ```

2. **Get All Subscriptions** - GET `/subscriptions`
   - Retrieves all airport subscriptions

3. **Unsubscribe from Airport** - DELETE `/subscriptions/{icaoCode}`
   - Removes an airport subscription
   - Replace `{icaoCode}` with the actual ICAO code (e.g., `LDZA`)

#### METAR Data
1. **Store METAR Data** - POST `/airport/{icaoCode}/METAR`
   - Stores METAR data for an airport
   - Replace `{icaoCode}` with the actual ICAO code
   - Request body example:
     ```json
     {
       "data": "METAR LDZA 301200Z 12008KT 9999 FEW040 25/18 Q1013 NOSIG"
     }
     ```

2. **Get Latest METAR Data** - GET `/airport/{icaoCode}/METAR`
   - Retrieves the latest METAR data for an airport
   - Replace `{icaoCode}` with the actual ICAO code

3. **Get Latest METAR Data with Selected Fields** - GET `/airport/{icaoCode}/METAR?fields=windSpeed,temperature`
   - Retrieves the latest METAR data with only specified fields
   - Replace `{icaoCode}` with the actual ICAO code
   - Modify the `fields` parameter to include desired fields

4. **Get Latest METAR Data Decoded** - GET `/airport/{icaoCode}/METAR?decoded=true`
   - Retrieves the latest METAR data in natural language format
   - Replace `{icaoCode}` with the actual ICAO code

## Testing Workflow

To test the complete workflow:

1. Start the Airport METAR Service application:
   ```bash
   ./mvnw spring-boot:run
   ```

2. In Postman, run the "Subscribe to Airport" request to add an airport subscription

3. Run the "Get All Subscriptions" request to verify the subscription was added

4. Run the "Store METAR Data" request to add METAR data for the subscribed airport

5. Run the "Get Latest METAR Data" request to retrieve the stored METAR data

6. Run the "Get Latest METAR Data Decoded" request to retrieve the stored METAR data in natural language format

7. Run the "Unsubscribe from Airport" request to remove the subscription

## Example ICAO Codes

For testing, you can use these ICAO codes:
- LDZA - Zagreb Airport (Croatia)
- LDSP - Split Airport (Croatia)
- EHAM - Amsterdam Airport Schiphol (Netherlands)
- LIRF - Rome Fiumicino Airport (Italy)
- LFPG - Paris Charles de Gaulle Airport (France)

## Troubleshooting

### Connection Refused
If you get connection refused errors:
1. Ensure the Airport METAR Service application is running
2. Check that the `base_url` variable matches where your service is running
3. Verify that your firewall isn't blocking connections

### 404 Not Found
If you get 404 errors when retrieving METAR data:
1. Ensure you've stored METAR data for that airport first
2. Verify the ICAO code is correct

### 409 Conflict
If you get 409 errors when subscribing:
1. The airport is already subscribed
2. Use the "Get All Subscriptions" request to see existing subscriptions