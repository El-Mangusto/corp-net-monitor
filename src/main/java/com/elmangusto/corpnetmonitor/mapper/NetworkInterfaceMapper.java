package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.enums.InterfaceType;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NetworkInterfaceMapper {

    public List<NetworkInterface> map(
            List<VariableBinding> nameBindings,
            List<VariableBinding> typeBindings,
            List<VariableBinding> speedBindings,
            Device device
    ) {
        List<NetworkInterface> interfaces = new ArrayList<>();

        for (int i = 0; i < nameBindings.size(); i++) {
            int ifIndex = nameBindings.get(i).getOid().last();
            String name = decodeSnmpString(nameBindings.get(i).getVariable().toString());
            int typeCode = typeBindings.get(i).getVariable().toInt();
            long speedBps = speedBindings.get(i).getVariable().toLong();
            InterfaceType type = InterfaceType.getByCode(typeCode);

            if (!isRealInterface(name, type, speedBps)) {
                continue;
            }

            interfaces.add(NetworkInterface.builder()
                    .device(device)
                    .ifIndex(ifIndex)
                    .name(name)
                    .type(type)
                    .speedBps(speedBps)
                    .build());
        }
        return interfaces;
    }

    private boolean isRealInterface(String name, InterfaceType type, long speed) {
        if (type == InterfaceType.LOOPBACK || speed <= 0) {
            return false;
        }

        String lower = name.toLowerCase();
        return !lower.contains("bluetooth") &&
                !lower.contains("virtualbox") &&
                !lower.contains("filter") &&
                !lower.contains("scheduler") &&
                !lower.contains("layer") &&
                !lower.contains("miniport") &&
                !lower.contains("ndis");
    }

    private String decodeSnmpString(String value) {
        if (value == null || value.isEmpty()) return "Unknown";

        if (value.matches("^([0-9a-fA-F]{2}:)+[0-9a-fA-F]{2}$")) {
            StringBuilder result = new StringBuilder();
            for (String part : value.split(":")) {
                int hexVal = Integer.parseInt(part, 16);
                if (hexVal > 0) {
                    result.append((char) hexVal);
                }
            }
            return result.toString().trim();
        }
        return value.trim();
    }
}