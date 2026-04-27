package com.elmangusto.corpnetmonitor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequest {
        @NotBlank(message = "IP address must not be blank")
        private String ipAddress;
}