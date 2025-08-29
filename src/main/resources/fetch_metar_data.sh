#!/bin/bash

# Script to fetch METAR data for subscribed airports and store it in the database
# This script should be run on a Linux system with access to the database and service

# Configuration
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="metar_service"
DB_USER="metar_user"
DB_PASSWORD="metar_password"
SERVICE_URL="http://localhost:8080"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to log messages
log_message() {
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Function to log success messages
log_success() {
    log_message "${GREEN}$1${NC}"
}

# Function to log warning messages
log_warning() {
    log_message "${YELLOW}$1${NC}"
}

# Function to log error messages
log_error() {
    log_message "${RED}$1${NC}"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check if required commands are available
if ! command_exists psql; then
    log_error "psql command not found. Please install PostgreSQL client."
    exit 1
fi

if ! command_exists curl; then
    log_error "curl command not found. Please install curl."
    exit 1
fi

# Function to get subscribed airports from database
get_subscribed_airports() {
    log_message "Retrieving subscribed airports from database..."
    
    # Query to get active subscriptions
    QUERY="SELECT icao_code FROM subscriptions WHERE active = true;"
    
    # Execute query and get results
    AIRPORTS=$(PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -t -c "$QUERY" 2>/dev/null | tr -d ' ')
    
    if [ -z "$AIRPORTS" ]; then
        log_warning "No subscribed airports found."
        return 1
    fi
    
    echo "$AIRPORTS"
    return 0
}

# Function to fetch METAR data for an airport
fetch_metar_data() {
    local ICAO_CODE=$1
    local NOAA_URL="https://tgftp.nws.noaa.gov/data/observations/metar/stations/${ICAO_CODE}.TXT"
    
    log_message "Fetching METAR data for $ICAO_CODE from $NOAA_URL"
    
    # Fetch data from NOAA
    local METAR_DATA=$(curl -s --max-time 30 "$NOAA_URL")
    
    # Check if we got data
    if [ -z "$METAR_DATA" ] || [[ "$METAR_DATA" == *"Not Found"* ]]; then
        log_warning "No METAR data found for $ICAO_CODE"
        return 1
    fi
    
    # Extract just the METAR line (second line of the response)
    local METAR_LINE=$(echo "$METAR_DATA" | sed -n '2p')
    
    if [ -z "$METAR_LINE" ]; then
        log_warning "Could not extract METAR data for $ICAO_CODE"
        return 1
    fi
    
    echo "$METAR_LINE"
    return 0
}

# Function to store METAR data in the service
store_metar_data() {
    local ICAO_CODE=$1
    local METAR_DATA=$2
    
    log_message "Storing METAR data for $ICAO_CODE"
    
    # Create JSON payload
    local JSON_PAYLOAD="{\"data\": \"$METAR_DATA\"}"
    
    # Send data to service
    local RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
        -H "Content-Type: application/json" \
        -d "$JSON_PAYLOAD" \
        "$SERVICE_URL/airport/$ICAO_CODE/METAR")
    
    if [ "$RESPONSE" -eq 201 ]; then
        log_success "Successfully stored METAR data for $ICAO_CODE"
        return 0
    else
        log_error "Failed to store METAR data for $ICAO_CODE. HTTP status: $RESPONSE"
        return 1
    fi
}

# Main function
main() {
    log_message "Starting METAR data fetch process..."
    
    # Get subscribed airports
    AIRPORTS=$(get_subscribed_airports)
    if [ $? -ne 0 ]; then
        log_warning "No airports to process. Exiting."
        exit 0
    fi
    
    local SUCCESS_COUNT=0
    local FAILURE_COUNT=0
    
    # Process each airport
    while IFS= read -r ICAO_CODE; do
        if [ -n "$ICAO_CODE" ]; then
            log_message "Processing airport: $ICAO_CODE"
            
            # Fetch METAR data
            METAR_DATA=$(fetch_metar_data "$ICAO_CODE")
            if [ $? -eq 0 ]; then
                # Store METAR data
                store_metar_data "$ICAO_CODE" "$METAR_DATA"
                if [ $? -eq 0 ]; then
                    ((SUCCESS_COUNT++))
                else
                    ((FAILURE_COUNT++))
                fi
            else
                ((FAILURE_COUNT++))
            fi
        fi
    done <<< "$AIRPORTS"
    
    log_message "Processing complete. Success: $SUCCESS_COUNT, Failures: $FAILURE_COUNT"
    
    if [ $FAILURE_COUNT -gt 0 ]; then
        log_warning "Some airports failed to process."
        exit 1
    else
        log_success "All airports processed successfully."
        exit 0
    fi
}

# Run main function
main "$@"