package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MetricLatestResponse {
    private LocalDateTime captureAt;
    private Long uptime;
    private Integer processes;
    private Double cpuLoadAvg;
    private List<StorageMetricResponse> storages;
    private List<NetworkMetricResponse> network;
}