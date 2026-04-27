package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MetricHistoryResponse {
    private LocalDateTime captureAt;
    private Long uptime;
    private Integer processes;
    private Double cpuLoadAvg;
}