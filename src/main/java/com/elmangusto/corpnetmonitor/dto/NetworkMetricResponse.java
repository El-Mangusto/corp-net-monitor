package com.elmangusto.corpnetmonitor.dto;

import com.elmangusto.corpnetmonitor.model.enums.InterfaceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NetworkMetricResponse {
    private String interfaceName;
    private InterfaceStatus status;
    private Long inSpeedKbps;
    private Long outSpeedKbps;
    private Double inUtilization;
    private Double outUtilization;
    private LocalDateTime captureAt;
}