package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.Metric;
import com.elmangusto.corpnetmonitor.model.StorageMetric;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StorageMapper implements SnmpMapper<List<StorageMetric>> {

    private static final double GB_FACTOR = 1024.0 * 1024.0 * 1024.0;

    @Override
    @SuppressWarnings("unchecked")
    public List<StorageMetric> map(Object... args) {

        List<VariableBinding> names = (List<VariableBinding>) args[0];
        List<VariableBinding> units = (List<VariableBinding>) args[1];
        List<VariableBinding> sizes = (List<VariableBinding>) args[2];
        List<VariableBinding> used = (List<VariableBinding>) args[3];
        Metric parent = (Metric) args[4];

        List<StorageMetric> storageMetrics = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            String storageName = names.get(i).getVariable().toString();

            if (storageName.contains(":\\") || storageName.equalsIgnoreCase("Physical Memory")) {
                long unitSize = units.get(i).getVariable().toLong();
                long totalBlocks = sizes.get(i).getVariable().toLong();
                long usedBlocks = used.get(i).getVariable().toLong();

                double totalGb = (totalBlocks * unitSize) / GB_FACTOR;
                double usedGb = (usedBlocks * unitSize) / GB_FACTOR;
                double percent = (totalBlocks > 0) ? (usedGb / totalGb) * 100 : 0;

                storageMetrics.add(StorageMetric.builder()
                        .metric(parent)
                        .name(storageName)
                        .type(storageName.contains(":\\") ? "Disk" : "RAM")
                        .totalSizeGb(round(totalGb))
                        .usedSizeGb(round(usedGb))
                        .usedPercent(round(percent))
                        .build());
            }
        }
        return storageMetrics;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}