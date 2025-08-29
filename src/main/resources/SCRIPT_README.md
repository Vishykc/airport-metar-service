# Automated METAR Data Fetching Script

This script automates the process of fetching METAR data for subscribed airports and storing it in the database.

## Prerequisites

- Linux operating system
- PostgreSQL client tools (`psql`)
- `curl` command-line tool
- Network access to:
  - PostgreSQL database
  - Airport METAR service REST API
  - NOAA METAR data service (https://tgftp.nws.noaa.gov)

Before running the script, make it executable:
```bash
chmod +x fetch_metar_data.sh
```

## Configuration

The script requires the following configuration parameters, which can be modified at the top of the script:

- `DB_HOST`: PostgreSQL database host (default: localhost)
- `DB_PORT`: PostgreSQL database port (default: 5432)
- `DB_NAME`: PostgreSQL database name (default: metar_service)
- `DB_USER`: PostgreSQL database user (default: metar_user)
- `DB_PASSWORD`: PostgreSQL database password (default: metar_password)
- `SERVICE_URL`: URL of the Airport METAR service (default: http://localhost:8080)

## Usage

1. Make the script executable:
   ```bash
   chmod +x fetch_metar_data.sh
   ```

2. Run the script:
   ```bash
   ./fetch_metar_data.sh
   ```

3. To run the script periodically, add it to crontab:
   ```bash
   # Run every hour
   0 * * * * /path/to/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1
   ```

## How It Works

1. The script connects to the PostgreSQL database to retrieve all active airport subscriptions
2. For each subscribed airport:
   - Fetches raw METAR data from the NOAA service
   - Extracts the METAR line from the response
   - Sends the raw METAR data to the Airport METAR service via REST API
3. Logs the results of each operation

## Output

The script provides colored output for different types of messages:
- Green: Success messages
- Yellow: Warnings
- Red: Errors
- White: Informational messages

All messages are timestamped for easier log analysis.

## Error Handling

The script includes error handling for:
- Missing dependencies (psql, curl)
- Database connection issues
- Network timeouts
- Invalid METAR data
- Service API errors

If any step fails for an airport, the script continues processing other airports and reports the overall success/failure count at the end.

## Scheduling

To run the script automatically at regular intervals, add it to your crontab:

```bash
# Edit crontab
crontab -e

# Add one of these lines:
# Run every 15 minutes
*/15 * * * * /path/to/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1

# Run every hour
0 * * * * /path/to/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1

# Run every 6 hours
0 */6 * * * /path/to/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1
```

Make sure to update the path to the script and log file location as appropriate for your system.

## Testing

To test the script manually:

1. Ensure the Airport METAR service is running:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Ensure PostgreSQL is running and accessible with the correct credentials

3. Subscribe to at least one airport using the service API:
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"icaoCode": "LDZA"}' http://localhost:8080/subscriptions
   ```

4. Run the script:
   ```bash
   ./fetch_metar_data.sh
   ```

5. Check that METAR data was stored by retrieving it:
   ```bash
   curl http://localhost:8080/airport/LDZA/METAR
   ```

The script should show success messages for each airport processed, and you should be able to retrieve the stored METAR data through the service API.