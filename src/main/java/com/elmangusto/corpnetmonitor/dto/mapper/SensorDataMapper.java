package com.elmangusto.corpnetmonitor.dto.mapper;

import com.elmangusto.corpnetmonitor.dto.SensorDataRequest;
import com.elmangusto.corpnetmonitor.dto.SensorDataResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class SensorDataMapper {

    private static final int SMOKE_THRESHOLD = 1000;

    public SensorDataResponse toResponse(SensorDataRequest request) {
        return SensorDataResponse.builder()
                .deviceId(request.getDeviceId())
                .temperatureC(request.getTemperatureC())
                .humidityPercent(request.getHumidityPercent())
                .smokeRawAdc(request.getSmokeRawAdc())
                .smokePercent(request.getSmokePercent())
                .smokeDetected(request.getSmokeRawAdc() > SMOKE_THRESHOLD)
                .receivedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }
}