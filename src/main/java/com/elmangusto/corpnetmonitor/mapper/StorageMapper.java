package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.Storage;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StorageMapper {

    private static final double GB_FACTOR = 1024.0 * 1024.0 * 1024.0;

    public List<Storage> mapStorages(
            List<VariableBinding> names,
            List<VariableBinding> units,
            List<VariableBinding> sizes,
            Device device
    ) {
        List<Storage> result = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            String storageName = names.get(i).getVariable().toString();

            if (!isRelevantStorage(storageName)) {
                continue;
            }

            long unitSize = units.get(i).getVariable().toLong();
            long totalBlocks = sizes.get(i).getVariable().toLong();
            double totalGb = round((totalBlocks * unitSize) / GB_FACTOR);
            String type = storageName.contains(":\\") ? "Disk" : "RAM";

            result.add(Storage.builder()
                    .device(device)
                    .name(storageName)
                    .type(type)
                    .totalSizeGb(totalGb)
                    .build());
        }
        return result;
    }

    private boolean isRelevantStorage(String name) {
        return name.contains(":\\") || name.equalsIgnoreCase("Physical Memory");
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}