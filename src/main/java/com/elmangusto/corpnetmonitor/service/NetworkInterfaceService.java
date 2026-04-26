package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.mapper.NetworkInterfaceMapper;
import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import com.elmangusto.corpnetmonitor.repository.NetworkInterfaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NetworkInterfaceService {

    private final NetworkInterfaceRepository repository;
    private final NetworkInterfaceMapper mapper;

    @Transactional
    public void syncInterfaces(Device device,
                               List<VariableBinding> names,
                               List<VariableBinding> types,
                               List<VariableBinding> speeds) {


        List<NetworkInterface> scannedInterfaces = mapper.map(names, types, speeds, device);

        for (NetworkInterface scanned : scannedInterfaces) {
            Optional<NetworkInterface> existingOpt = repository.findByDeviceAndIfIndex(device, scanned.getIfIndex());

            if (existingOpt.isPresent()) {
                NetworkInterface existing = existingOpt.get();
                existing.setName(scanned.getName());
                existing.setType(scanned.getType());
                existing.setSpeedBps(scanned.getSpeedBps());
                repository.save(existing);
            } else {
                repository.save(scanned);
            }
        }
    }
}