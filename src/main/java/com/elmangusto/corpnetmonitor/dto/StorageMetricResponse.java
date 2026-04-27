package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StorageMetricResponse {
    private String name;
    private String type;
    private Double totalSizeGb;
    private Double usedSizeGb;
    private Double usedPercent;
    private LocalDateTime captureAt;
}