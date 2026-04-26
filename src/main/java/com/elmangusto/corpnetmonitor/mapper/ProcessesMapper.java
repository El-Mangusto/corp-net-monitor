package com.elmangusto.corpnetmonitor.mapper;

import org.springframework.stereotype.Component;

@Component
public class ProcessesMapper implements SnmpMapper<Integer>{
    @Override
    public Integer map(Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            return 0;
        }

        String value = args[0].toString();

        if (value.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(value);
    }
}
