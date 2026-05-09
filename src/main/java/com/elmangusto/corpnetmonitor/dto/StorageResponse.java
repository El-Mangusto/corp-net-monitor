package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageResponse {
    private Long id;
    private String name;
    private String type;
    private Double totalSizeGb;
}