# Scheduling the METAR Fetch Script with WSL 2 Ubuntu on Windows 11

This document explains how to schedule the `fetch_metar_data.sh` script to run every minute using WSL 2 Ubuntu while your Spring Boot application runs on Windows 11.

## Prerequisites

1. WSL 2 with Ubuntu installed on Windows 11
2. The Airport METAR Service Spring Boot application running on Windows 11
3. PostgreSQL database accessible from WSL 2
4. Network connectivity between WSL 2 and Windows 11

## Understanding the Network Setup

When using WSL 2, there are two important network considerations:

1. **Accessing Windows localhost from WSL 2**: Use `host.docker.internal` or the Windows host IP
2. **Accessing WSL 2 services from Windows**: Use the WSL 2 IP address

## Configuration Steps

### 1. Update the Script Configuration

First, modify the `fetch_metar_data.sh` script to connect to your Windows-hosted services:

```bash
# Edit the script configuration section
nano src/main/resources/fetch_metar_data.sh
```

Update these lines:
```bash
# Configuration - Update these for your Windows setup
DB_HOST="host.docker.internal"  # or your Windows IP address
DB_PORT="5432"
DB_NAME="metar_service_dev"
DB_USER="metar_user_dev"
DB_PASSWORD="metar_password_dev"
SERVICE_URL="http://host.docker.internal:8080"  # Windows Spring Boot app
```

To find your Windows IP address from WSL 2:
```bash
# In WSL terminal
cat /etc/resolv.conf | grep nameserver
```

The nameserver IP is usually your Windows host IP.

### 2. Make the Script Executable

```bash
# In WSL terminal, navigate to your project directory
chmod +x src/main/resources/fetch_metar_data.sh
```

### 3. Test the Script Manually

Before scheduling, test the script manually:

```bash
# In WSL terminal
./src/main/resources/fetch_metar_data.sh
```

### 4. Schedule with Cron

Open the crontab editor in WSL:

```bash
# In WSL terminal
crontab -e
```

Add this line to run the script every minute:
```bash
* * * * * cd /mnt/d/path/to/your/project && ./src/main/resources/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1
```
# if problems with permissions, add this to crontab -e
# * * * * * cd /mnt/d/Vishy/Posao_after_2_8_2025/2e_Systems/airport-metar-service && ./src/main/resources/fetch_metar_data.sh >> /home/vishykc/metar_fetch.log 2>&1

Make sure to:
1. Replace `/mnt/d/path/to/your/project` with the actual path to your project in WSL
2. The path should match where your Windows project is mounted in WSL

### 5. Alternative: Using Windows Task Scheduler

If you prefer to use Windows Task Scheduler:

1. Open Task Scheduler in Windows
2. Create a new task
3. Set the trigger to run every minute
4. Set the action to run a program with these parameters:
   - Program: `wsl`
   - Arguments: `-e bash -c "cd /mnt/d/path/to/your/project && ./src/main/resources/fetch_metar_data.sh >> /var/log/metar_fetch.log 2>&1"`

### 6. Verify the Setup

Check that the cron job is scheduled:
```bash
# In WSL terminal
crontab -l
```

Monitor the logs:
```bash
# In WSL terminal
tail -f /var/log/metar_fetch.log
```

## Troubleshooting

### Common Issues

1. **Connection Refused**: Ensure your Spring Boot application is configured to accept connections from external hosts:
   - In `application.properties`, make sure you have: `server.address=0.0.0.0`
   - Check Windows Firewall settings

2. **Database Connection Failed**: 
   - Ensure PostgreSQL is configured to accept connections from WSL 2
   - Check `pg_hba.conf` and `postgresql.conf` files

3. **Path Issues**: 
   - Make sure the path in the cron job matches where your project is located in WSL
   - Windows drives are mounted under `/mnt/[drive-letter]` in WSL

### Testing Network Connectivity

Test database connection from WSL:
```bash
# In WSL terminal
psql -h host.docker.internal -p 5432 -U metar_user_dev -d metar_service_dev
```

Test Spring Boot service from WSL:
```bash
# In WSL terminal
curl http://host.docker.internal:8080/actuator/health
```

## Monitoring and Maintenance

### Log Rotation

Set up log rotation to prevent the log file from growing too large:

```bash
# Create logrotate configuration
sudo nano /etc/logrotate.d/metar-fetch
```

Add this content:
```
/var/log/metar_fetch.log {
    daily
    rotate 7
    compress
    delaycompress
    missingok
    notifempty
    copytruncate
}
```

### Service Health Checks

You can add health checks to your script by modifying it to check if the service is running before attempting to fetch data:

```bash
# Add this function to fetch_metar_data.sh
check_service_health() {
    if curl -s -f "$SERVICE_URL/actuator/health" > /dev/null; then
        return 0
    else
        log_error "Service is not responding at $SERVICE_URL"
        return 1
    fi
}
```

Call this function at the beginning of the main function:
```bash
# In the main function, add after logging start message
if ! check_service_health; then
    log_error "Aborting script execution due to service unavailability"
    exit 1
fi
```

This setup will ensure that your METAR data fetching script runs every minute using WSL 2 while connecting to your Spring Boot application running on Windows 11.