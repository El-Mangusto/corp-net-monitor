package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.InterfaceType;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NetworkInterfaceMapper implements SnmpMapper<List<NetworkInterface>> {

    @Override
    @SuppressWarnings("unchecked")
    public List<NetworkInterface> map(Object... args) {
        if (args == null || args.length < 4) {
            return new ArrayList<>();
        }

        List<VariableBinding> nameBindings = (List<VariableBinding>) args[0];
        List<VariableBinding> typeBindings = (List<VariableBinding>) args[1];
        List<VariableBinding> speedBindings = (List<VariableBinding>) args[2];
        Device device = (Device) args[3];

        List<NetworkInterface> interfaces = new ArrayList<>();

        for (int i = 0; i < nameBindings.size(); i++) {
            int ifIndex = nameBindings.get(i).getOid().last();

            String rawName = nameBindings.get(i).getVariable().toString();
            String decodedName = decodeSnmpString(rawName);

            int typeCode = typeBindings.get(i).getVariable().toInt();
            long speedBps = speedBindings.get(i).getVariable().toLong();

            InterfaceType type = InterfaceType.getByCode(typeCode);

            if (isRealInterface(decodedName, type, speedBps)) {
                interfaces.add(NetworkInterface.builder()
                        .device(device)
                        .ifIndex(ifIndex)
                        .name(decodedName)
                        .type(type)
                        .speedBps(speedBps)
                        .build());
            }
        }
        return interfaces;
    }

    private boolean isRealInterface(String name, InterfaceType type, long speed) {
        String lowerName = name.toLowerCase();

        if (type == InterfaceType.LOOPBACK || speed <= 0) {
            return false;
        }

        if (lowerName.contains("bluetooth")) {
            return false;
        }

        if (lowerName.contains("virtualbox")) {
            return false;
        }

        if (lowerName.contains("filter") ||
                lowerName.contains("scheduler") ||
                lowerName.contains("layer") ||
                lowerName.contains("miniport") ||
                lowerName.contains("ndis")) {
            return false;
        }

        return true;
    }

    private String decodeSnmpString(String value) {
        if (value == null || value.isEmpty()) return "Unknown";

        if (value.matches("^([0-9a-fA-F]{2}:)+[0-9a-fA-F]{2}$")) {
            StringBuilder result = new StringBuilder();
            String[] parts = value.split(":");
            for (String part : parts) {
                int hexVal = Integer.parseInt(part, 16);
                if (hexVal > 0) result.append((char) hexVal);
            }
            return result.toString().trim();
        }
        return value.trim();
    }
}