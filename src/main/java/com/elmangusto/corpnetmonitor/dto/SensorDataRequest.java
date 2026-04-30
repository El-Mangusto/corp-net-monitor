package com.elmangusto.corpnetmonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorDataRequest {

    @NotBlank(message = "deviceId must not be blank")
    private String deviceId;

    @NotNull
    private Double temperatureC;

    @NotNull
    private Double humidityPercent;

    @NotNull
    private Integer smokeRawAdc;

    @NotNull
    private Double smokePercent;
}