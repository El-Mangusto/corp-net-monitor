package com.elmangusto.corpnetmonitor.dto;

import com.elmangusto.corpnetmonitor.model.enums.InterfaceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetworkInterfaceResponse {
    private Long id;
    private Integer ifIndex;
    private String name;
    private InterfaceType type;
    private Long speedBps;
}