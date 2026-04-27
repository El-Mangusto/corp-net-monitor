package com.elmangusto.corpnetmonitor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceResponse {
    private Long id;
    private String ipAddress;
    private String name;
    private String software;
}