package com.elmangusto.corpnetmonitor.dto.mapper;

import com.elmangusto.corpnetmonitor.dto.DeviceResponse;
import com.elmangusto.corpnetmonitor.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceDtoMapper {

    public DeviceResponse toResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .ipAddress(device.getIpAddress())
                .name(device.getName())
                .software(device.getSoftware())
                .build();
    }
}
