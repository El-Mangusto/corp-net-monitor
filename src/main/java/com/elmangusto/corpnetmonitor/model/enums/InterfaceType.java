package com.elmangusto.corpnetmonitor.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterfaceType {
    ETHERNET(6, "Ethernet"),
    WIFI(71, "Wi-Fi"),
    LOOPBACK(24, "Loopback"),
    TUNNEL(131, "Tunnel/VPN"),
    UNKNOWN(0, "Unknown");

    private final int snmpCode;
    private final String description;

    public static InterfaceType getByCode(int code) {
        for (InterfaceType type : values()) {
            if (type.snmpCode == code) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
