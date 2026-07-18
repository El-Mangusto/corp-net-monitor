# Corp Net Monitor

Backend service for monitoring corporate network infrastructure — collects device health metrics over **SNMP** (CPU load, storage, network interface traffic) and environmental readings from **physical sensors** (temperature, humidity, smoke), then exposes it all through a REST API.

Built as a bachelor's thesis project (Computer Engineering, network hardware monitoring), with an ESP32 + DHT22 + MQ-2 sensor node simulated in Wokwi feeding data into the backend.

**Frontend:** a companion dashboard (Vite + Chart.js) that visualizes the collected metrics — [corp-net-monitor-front](https://github.com/El-Mangusto/copr-net-monitor-front).

## Features

- **SNMP polling** — periodic collection of device metrics via SNMPv2c (`SnmpManager`, built on SNMP4J), supporting both single-value `GET` and subtree `WALK` operations.
- **Scheduled monitoring** — a background scheduler (`MonitoringScheduler`) scans all registered devices on a fixed interval and persists the results.
- **Metric domains** — system info (uptime, description), CPU load, storage usage, and per-interface network traffic (in/out octets, speed, operational status), each with its own repository, mapper, and history endpoint.
- **Sensor ingestion** — a REST endpoint for external hardware (ESP32-class devices) to push temperature/humidity/smoke readings, decoupled from the SNMP collection pipeline.
- **Device management** — CRUD for monitored devices (IP address, name, discovered software).
- **Centralized error handling** — a `@RestControllerAdvice` global exception handler mapping domain exceptions (SNMP timeouts, not-found, validation) to consistent HTTP responses.

## Tech Stack

- Java 21, Spring Boot (Web, Data JPA)
- PostgreSQL
- SNMP4J
- Lombok, MapStruct-style manual DTO mappers
- Maven

## Architecture

Standard layered structure, split by domain:

```
controller/   REST endpoints (devices, metrics, interfaces, storage, sensors)
service/      business logic + SNMP orchestration
collector/    SNMP transport layer (SnmpManager, SnmpMetric OIDs)
scheduler/    periodic polling job
repository/   Spring Data JPA repositories
model/        JPA entities + enums (InterfaceStatus, InterfaceType)
dto/          request/response DTOs + mappers
exceptions/   domain exceptions + global handler
```

## API Overview

| Endpoint | Description |
|---|---|
| `GET/POST/PUT/DELETE /api/devices` | Manage monitored devices |
| `GET /api/devices/{id}/metrics/latest` | Latest system metrics for a device |
| `GET /api/devices/{id}/metrics/history` | Historical metrics (filterable by time range/limit) |
| `GET /api/devices/{id}/metrics/storage/history` | Storage usage history |
| `GET /api/devices/{id}/metrics/network/history` | Network traffic history |
| `GET /api/devices/{id}/interfaces` | Network interfaces discovered on a device |
| `GET /api/devices/{id}/storages` | Storage volumes discovered on a device |
| `POST /api/sensors` | Ingest a reading from an external sensor node |
| `GET /api/sensors/{deviceId}/latest` | Latest sensor reading for a device |

## Running Locally

**Prerequisites:** Java 21, Maven, PostgreSQL

1. Create a database:
   ```sql
   CREATE DATABASE corp_net_monitor;
   ```
2. Configure `src/main/resources/application.properties` with your PostgreSQL credentials (or override via environment variables).
3. Run:
   ```bash
   ./mvnw spring-boot:run
   ```

The service starts on the default Spring Boot port (`8080`) and begins polling registered devices every 15 seconds.

## Notes

This is a portfolio/thesis project — SNMP polling assumes devices reachable over UDP/161 with community string `public` (SNMPv2c). Not hardened for production use (no auth on the API, no SNMPv3 support yet).
