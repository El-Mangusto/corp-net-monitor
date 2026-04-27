package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.*;
import com.elmangusto.corpnetmonitor.model.enums.InterfaceStatus;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NetworkMetricMapper {

    private static final double POLLING_INTERVAL = 15.0;
    private static final long MAX_COUNTER_32 = 4294967295L;

    public List<NetworkMetric> map(
            List<VariableBinding> inOctets,
            List<VariableBinding> outOctets,
            List<VariableBinding> statuses,
            Metric parentMetric,
            List<NetworkInterface> dbInterfaces
    ) {
        List<NetworkMetric> results = new ArrayList<>();

        for (NetworkInterface ni : dbInterfaces) {
            VariableBinding inVar = findByIfIndex(inOctets, ni.getIfIndex());
            VariableBinding outVar = findByIfIndex(outOctets, ni.getIfIndex());
            VariableBinding statVar = findByIfIndex(statuses, ni.getIfIndex());

            if (inVar == null || outVar == null) {
                continue;
            }

            long currentIn = inVar.getVariable().toLong();
            long currentOut = outVar.getVariable().toLong();

            int statusCode = (statVar != null) ? statVar.getVariable().toInt() : 0;
            InterfaceStatus status = InterfaceStatus.getByCode(statusCode);

            long deltaIn = calculateDelta(currentIn, ni.getLastInOctets());
            long deltaOut = calculateDelta(currentOut, ni.getLastOutOctets());

            long inSpeedKbps = Math.round((deltaIn * 8.0) / POLLING_INTERVAL / 1024.0);
            long outSpeedKbps = Math.round((deltaOut * 8.0) / POLLING_INTERVAL / 1024.0);

            double inUtil = calcUtilization(inSpeedKbps, ni.getSpeedBps());
            double outUtil = calcUtilization(outSpeedKbps, ni.getSpeedBps());

            ni.setLastInOctets(currentIn);
            ni.setLastOutOctets(currentOut);

            results.add(NetworkMetric.builder()
                    .metric(parentMetric)
                    .networkInterface(ni)
                    .status(status)
                    .inSpeedKbps(inSpeedKbps)
                    .outSpeedKbps(outSpeedKbps)
                    .inUtilization(inUtil)
                    .outUtilization(outUtil)
                    .build());
        }
        return results;
    }

    private double calcUtilization(long speedKbps, long ifSpeedBps) {
        if (ifSpeedBps <= 0) return 0.0;
        double util = (speedKbps * 1024.0 * 100.0) / ifSpeedBps;
        return Math.min(Math.round(util * 100.0) / 100.0, 100.0);
    }

    private long calculateDelta(long current, Long previous) {
        if (previous == null || previous == 0) return 0;
        return (current >= previous)
                ? current - previous
                : (MAX_COUNTER_32 - previous) + current;
    }

    private VariableBinding findByIfIndex(List<VariableBinding> list, int index) {
        return list.stream()
                .filter(vb -> vb.getOid().last() == index)
                .findFirst()
                .orElse(null);
    }
}