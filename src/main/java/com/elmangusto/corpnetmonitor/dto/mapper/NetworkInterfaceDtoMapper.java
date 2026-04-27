package com.elmangusto.corpnetmonitor.dto.mapper;

import com.elmangusto.corpnetmonitor.dto.NetworkInterfaceResponse;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import org.springframework.stereotype.Component;

@Component
public class NetworkInterfaceDtoMapper {

    public NetworkInterfaceResponse toResponse(NetworkInterface networkInterface) {
        return NetworkInterfaceResponse.builder()
                .id(networkInterface.getId())
                .ifIndex(networkInterface.getIfIndex())
                .name(networkInterface.getName())
                .type(networkInterface.getType())
                .speedBps(networkInterface.getSpeedBps())
                .build();
    }
}
