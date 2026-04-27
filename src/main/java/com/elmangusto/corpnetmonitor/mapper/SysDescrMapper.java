package com.elmangusto.corpnetmonitor.mapper;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SysDescrMapper {

    private static final Map<String, String> OS_MAP = Map.of(
            "windows", "Windows",
            "linux", "Linux",
            "cisco", "Cisco IOS",
            "unix", "Unix",
            "freebsd", "FreeBSD"
    );

    public String map(String value) {
        if (value == null || value.isBlank()) return "Unknown";

        String lower = value.toLowerCase();

        return OS_MAP.entrySet().stream()
                .filter(entry -> lower.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Unknown");
    }
}