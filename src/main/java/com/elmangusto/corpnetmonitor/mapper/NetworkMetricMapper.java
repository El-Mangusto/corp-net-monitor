package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.*;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NetworkMetricMapper implements SnmpMapper<List<NetworkMetric>> {

    private static final double POLLING_INTERVAL = 15.0;
    private static final long MAX_COUNTER_32 = 4294967295L;

    @Override
    @SuppressWarnings("unchecked")
    public List<NetworkMetric> map(Object... args) {
        List<VariableBinding> inOctets = (List<VariableBinding>) args[0];
        List<VariableBinding> outOctets = (List<VariableBinding>) args[1];
        List<VariableBinding> statuses = (List<VariableBinding>) args[2];
        Metric parentMetric = (Metric) args[3];
        List<NetworkInterface> dbInterfaces = (List<NetworkInterface>) args[4];

        List<NetworkMetric> results = new ArrayList<>();

        for (NetworkInterface ni : dbInterfaces) {
            VariableBinding inVar = findByIfIndex(inOctets, ni.getIfIndex());
            VariableBinding outVar = findByIfIndex(outOctets, ni.getIfIndex());
            VariableBinding statVar = findByIfIndex(statuses, ni.getIfIndex());

            if (inVar == null || outVar == null) continue;

            long currentIn = inVar.getVariable().toLong();
            long currentOut = outVar.getVariable().toLong();

            int statusCode = (statVar != null) ? statVar.getVariable().toInt() : 0;
            InterfaceStatus status = InterfaceStatus.getByCode(statusCode);

            long deltaIn = calculateDelta(currentIn, ni.getLastInOctets());
            long deltaOut = calculateDelta(currentOut, ni.getLastOutOctets());

            long inSpeedKbps = Math.round((deltaIn * 8.0) / POLLING_INTERVAL / 1024.0);
            long outSpeedKbps = Math.round((deltaOut * 8.0) / POLLING_INTERVAL / 1024.0);

            double inUtil = (ni.getSpeedBps() > 0) ? (inSpeedKbps * 1024.0 * 100.0) / ni.getSpeedBps() : 0.0;
            double outUtil = (ni.getSpeedBps() > 0) ? (outSpeedKbps * 1024.0 * 100.0) / ni.getSpeedBps() : 0.0;

            inUtil = Math.round(inUtil * 100.0) / 100.0;
            outUtil = Math.round(outUtil * 100.0) / 100.0;

            ni.setLastInOctets(currentIn);
            ni.setLastOutOctets(currentOut);

            results.add(NetworkMetric.builder()
                    .metric(parentMetric)
                    .networkInterface(ni)
                    .status(status)
                    .inSpeedKbps(inSpeedKbps)
                    .outSpeedKbps(outSpeedKbps)
                    .inUtilization(Math.min(inUtil, 100.0))
                    .outUtilization(Math.min(outUtil, 100.0))
                    .build());
        }
        return results;
    }

    private long calculateDelta(long current, Long previous) {
        if (previous == null || previous == 0) return 0;

        if (current >= previous) {
            return current - previous;
        } else {
            return (MAX_COUNTER_32 - previous) + current;
        }
    }

    private VariableBinding findByIfIndex(List<VariableBinding> list, int index) {
        return list.stream()
                .filter(vb -> vb.getOid().last() == index)
                .findFirst()
                .orElse(null);
    }
}