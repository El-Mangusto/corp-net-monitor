package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.dto.NetworkInterfaceResponse;
import com.elmangusto.corpnetmonitor.dto.mapper.NetworkInterfaceDtoMapper;
import com.elmangusto.corpnetmonitor.mapper.NetworkInterfaceMapper;
import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import com.elmangusto.corpnetmonitor.repository.DeviceRepository;
import com.elmangusto.corpnetmonitor.repository.NetworkInterfaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final NetworkInterfaceDtoMapper dtoMapper;
    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public List<NetworkInterfaceResponse> getInterfacesByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));

        return repository.findByDevice(device).stream()
                .map(dtoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NetworkInterfaceResponse getInterface(Long deviceId, Long interfaceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));

        NetworkInterface networkInterface = repository.findById(interfaceId)
                .orElseThrow(() -> new EntityNotFoundException("Interface not found with id: " + interfaceId));

        if (!networkInterface.getDevice().getId().equals(device.getId())) {
            throw new EntityNotFoundException("Interface " + interfaceId + " does not belong to device " + deviceId);
        }

        return dtoMapper.toResponse(networkInterface);
    }

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