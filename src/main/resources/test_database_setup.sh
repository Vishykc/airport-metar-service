#!/bin/bash

# Script to test database setup for Airport METAR Service
# This script verifies that the database and user have been set up correctly

# Configuration - update these values if you've changed them in application.properties
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="metar_service"
DB_USER="metar_user"
DB_PASSWORD="metar_password"

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

log_message "Testing database setup for Airport METAR Service..."

# Test 1: Check if database exists
log_message "Test 1: Checking if database exists..."
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT version();" >/dev/null 2>&1
if [ $? -eq 0 ]; then
    log_success "✓ Database connection successful"
else
    log_error "✗ Cannot connect to database. Please check:"
    echo "  - Is PostgreSQL running?"
    echo "  - Are the connection details correct?"
    echo "  - Did you run the sql_script_for_creating_database_and_user.sql script?"
    exit 1
fi

# Test 2: Check user permissions
log_message "Test 2: Checking user permissions..."
PERMISSION_CHECK=$(PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "CREATE TABLE test_permission (id SERIAL PRIMARY KEY, test_data VARCHAR(50));" 2>&1)
if [[ $PERMISSION_CHECK == *"CREATE TABLE"* ]]; then
    log_success "✓ User has permission to create tables"
    # Clean up test table
    PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "DROP TABLE test_permission;" >/dev/null 2>&1
else
    log_error "✗ User does not have permission to create tables"
    echo "  Please ensure you've run the complete sql_script_for_creating_database_and_user.sql"
    echo "  which includes the schema permission grants."
    exit 1
fi

# Test 3: Check schema permissions
log_message "Test 3: Checking schema permissions..."
SCHEMA_CHECK=$(PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT has_schema_privilege('metar_user', 'public', 'CREATE');" -t 2>/dev/null | tr -d ' ')
if [[ $SCHEMA_CHECK == "t" ]]; then
    log_success "✓ User has CREATE permission on public schema"
else
    log_error "✗ User does not have CREATE permission on public schema"
    echo "  Please ensure you've run the complete sql_script_for_creating_database_and_user.sql"
    echo "  which includes the schema permission grants."
    exit 1
fi

log_success "All database setup tests passed!"
echo ""
echo "You can now start the Airport METAR Service application with:"
echo "  ./mvnw spring-boot:run"
echo ""
echo "Or build and run the JAR:"
echo "  ./mvnw clean package"
echo "  java -jar target/airport-metar-service-0.0.1-SNAPSHOT.jar"