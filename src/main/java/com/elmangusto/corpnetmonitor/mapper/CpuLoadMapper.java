package com.elmangusto.corpnetmonitor.mapper;

import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CpuLoadMapper {

    public Double map(List<VariableBinding> cpuCores) {
        if (cpuCores == null || cpuCores.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (VariableBinding core : cpuCores) {
            sum += core.getVariable().toLong();
        }

        double average = sum / cpuCores.size();
        return Math.round(average * 100.0) / 100.0;
    }
}
