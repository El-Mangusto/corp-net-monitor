package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.exceptions.SnmpMapperException;
import org.springframework.stereotype.Component;

@Component
public class SysDescrMapper implements SnmpMapper<String> {

    @Override
    public String map(Object... args) {
        if (args == null || args.length == 0 || args[0] == null) {
            return "Unknown";
        }

        String value = args[0].toString();

        if (value.isEmpty()) {
            return "Unknown";
        }

        try {
            String lower = value.toLowerCase();

            if (lower.contains("windows")) {
                return "Windows";
            } else if (lower.contains("linux")) {
                return "Linux";
            } else if (lower.contains("cisco")) {
                return "Cisco IOS";
            } else if (lower.contains("unix")) {
                return "Unix";
            } else if (lower.contains("freebsd")) {
                return "FreeBSD";
            } else {
                return "Unknown";
            }

        } catch (Exception e) {
            throw new SnmpMapperException("SysDescrMapper failed to parse value [" + value + "]: " + e.getMessage());
        }
    }
}