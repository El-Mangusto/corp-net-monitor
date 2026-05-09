package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.Metric;
import com.elmangusto.corpnetmonitor.model.Storage;
import com.elmangusto.corpnetmonitor.model.StorageMetric;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StorageMetricMapper {

    private static final double GB_FACTOR = 1024.0 * 1024.0 * 1024.0;

    public List<StorageMetric> map(
            List<VariableBinding> names,
            List<VariableBinding> units,
            List<VariableBinding> sizes,
            List<VariableBinding> used,
            Metric parent,
            List<Storage> dbStorages
    ) {
        Map<String, Storage> storageByName = dbStorages.stream()
                .collect(Collectors.toMap(Storage::getName, s -> s));

        List<StorageMetric> result = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            String storageName = names.get(i).getVariable().toString();

            Storage storage = storageByName.get(storageName);
            if (storage == null) {
                continue;
            }

            long unitSize = units.get(i).getVariable().toLong();
            long totalBlocks = sizes.get(i).getVariable().toLong();
            long usedBlocks = used.get(i).getVariable().toLong();

            double totalGb = (totalBlocks * unitSize) / GB_FACTOR;
            double usedGb = (usedBlocks * unitSize) / GB_FACTOR;
            double percent = (totalBlocks > 0) ? (usedGb / totalGb) * 100 : 0;

            result.add(StorageMetric.builder()
                    .metric(parent)
                    .storage(storage)
                    .usedSizeGb(round(usedGb))
                    .usedPercent(round(percent))
                    .build());
        }
        return result;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}