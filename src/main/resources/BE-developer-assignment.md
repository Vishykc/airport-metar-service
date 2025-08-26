
# BE Developer Candidate Practical Assignment

## Task Summary
Develop a service that continuously loads and stores a subset of METAR data for the subscribed airports, 
and makes that data available to clients on request.

## Domain Knowledge
To familiarise yourself with the domain, please consult the following resources:

- [METAR on Wikipedia](https://en.wikipedia.org/wiki/METAR)
- [ICAO airport codes on Wikipedia](https://en.wikipedia.org/wiki/ICAO_airport_code)

## Required Technologies
- The service must be implemented using Java 8 (or newer), and based on the Spring framework (preferably Spring Boot).
- Storing of METAR data must be done in a RDBMS, and managed using JPA.
- All components of the solution (the service, RDBMS, automated tasks) must run under the Linux operating system.
- The solution should be made available in a public source code repository (GitHub, Bitbucket, GitLab, or similar).

## Task Details
The solution consists of 3 components, described in the following sections.

### The Service
The service is a Java application running continuously that provides, at a minimum, two RESTful endpoints, as follows:

- **Airport subscription/unsubscription and subscription listing endpoint** provides support for `POST`, `GET`, and `DELETE` methods.  
  Examples:
  - `POST /subscriptions` with JSON payload `{ "icaoCode": "LDZA" }` subscribes the Zagreb airport.
  - `GET /subscriptions` returns all subscribed airports in JSON, e.g. `[ {"icaoCode": "LDZA"} ]`.
  - `DELETE /subscriptions/LDZA` deletes the subscription for Zagreb airport.

- **METAR data storage/retrieval service**. Examples:
  - `POST /airport/LDZA/METAR` with JSON payload `{ "data": "METAR LDZA 121200Z 0902MPS 090V150 2000 R04/P2000N R22/P2000N OVC050 0/M01 Q1020=" }` stores the data in the database.
  - `GET /airport/LDZA/METAR` returns the last METAR data record stored in JSON format.

### Automated Task
This consists of a job, scheduled to run in regular intervals, that retrieves a list of subscribed airports, queries an external service for METAR data, and sends the retrieved data to the METAR storage service.

External service for METAR data retrieval:  
[NOAA METAR Data Service](https://tgftp.nws.noaa.gov/data/observations/metar/stations/)

The job should be implemented and scheduled from the Linux shell (command line), as a script or a list of commands.

### The Database
The database contains, at a minimum, the following tables:

- `subscriptions` table stores all subscribed airports.
- `metar` table stores METAR data for airports.

## Extra Tasks
Before implementing any of the following extras, please tag the basic version of the code in your source control system.  
Then, tag a new version after implementing any of the following:

- Extend the subscription endpoint to support activation/deactivation with the `PUT` method, e.g.  
  `PUT /subscriptions/LDZA` with JSON payload `{ "active": "0" }` deactivates Zagreb airport subscription.

- Provide filtering capabilities when returning airport subscriptions (e.g., only active ones or those matching certain letters).

- Parse and split METAR data, storing elements in separate fields (e.g., timestamp, wind strength, temperature, visibility).

- Extend the METAR retrieval endpoint to allow retrieving only a subset of available data (e.g., wind strength and temperature).

- Decode METAR data into natural language, either before storing it or dynamically when returning it.
