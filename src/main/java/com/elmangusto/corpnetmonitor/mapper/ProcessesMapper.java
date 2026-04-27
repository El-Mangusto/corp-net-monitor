package com.elmangusto.corpnetmonitor.mapper;

import org.springframework.stereotype.Component;

@Component
public class ProcessesMapper {

    public Integer map(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return Integer.parseInt(value);
    }
}
