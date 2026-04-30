package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SensorDataResponse {
    private String deviceId;
    private Double temperatureC;
    private Double humidityPercent;
    private Integer smokeRawAdc;
    private Double smokePercent;
    private boolean smokeDetected;
    private LocalDateTime receivedAt;
}