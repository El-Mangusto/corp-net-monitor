package com.elmangusto.corpnetmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum InterfaceStatus {
    UP(1, "UP"),
    DOWN(2, "DOWN"),
    TESTING(3, "TESTING"),
    UNKNOWN(0, "UNKNOWN");

    private final int code;
    private final String description;

    public static InterfaceStatus getByCode(int code) {
        return Arrays.stream(InterfaceStatus.values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
