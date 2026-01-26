# Monitoring stack (Prometheus & Grafana)

Application exposes metrics via Spring Boot Actuator and Micrometer.

## Start monitoring stack:

### Using bat script (Windows):

From project [root](..) folder:

    run-monitoring.bat

### Or manually:

From [grafana](.) folder:

    docker-compose up

## Services:

After starting, the following services will be available:

- **Prometheus**: http://localhost:9090
  - Scrapes metrics from application at `http://host.docker.internal:8099/actuator/prometheus`
  - Scrape interval: 5 seconds

- **Grafana**: http://localhost:3000
  - Default credentials: `admin` / `admin`
  - Pre-configured datasource: Prometheus
  - Pre-configured dashboards:
    - JVM Micrometer metrics
    - Spring Boot throughput metrics

## Application metrics endpoints:

- Health check: http://localhost:8099/actuator/health
- Prometheus metrics: http://localhost:8099/actuator/prometheus

**Note**: Make sure the application is running on port 8099 before starting the monitoring stack.

## Destroy monitoring containers and volumes (with stored data):

From [grafana](.) folder:

    docker-compose down --volumes
