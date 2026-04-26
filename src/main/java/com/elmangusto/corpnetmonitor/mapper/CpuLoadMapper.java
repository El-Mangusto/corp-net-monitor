package com.elmangusto.corpnetmonitor.mapper;

import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CpuLoadMapper implements SnmpMapper<Double>{
    @Override
    @SuppressWarnings("unchecked")
    public Double map(Object... args) {

        List<VariableBinding> cpuCores = (List<VariableBinding>) args[0];

        if (cpuCores == null || cpuCores.isEmpty()) {
            return 0.0;
        }

        double sumPercentCores = 0;
        for (VariableBinding cpuCore : cpuCores) {
            sumPercentCores += cpuCore.getVariable().toLong();
        }

        double average = sumPercentCores / cpuCores.size();

        return Math.round(average * 100.0) / 100.0;
    }
}
