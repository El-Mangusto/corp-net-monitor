package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.collector.SnmpManager;
import com.elmangusto.corpnetmonitor.dto.DeviceRequest;
import com.elmangusto.corpnetmonitor.dto.DeviceResponse;
import com.elmangusto.corpnetmonitor.dto.mapper.DeviceDtoMapper;
import com.elmangusto.corpnetmonitor.exceptions.SnmpServiceException;
import com.elmangusto.corpnetmonitor.mapper.SysDescrMapper;
import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.collector.SnmpMetric;
import com.elmangusto.corpnetmonitor.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final SnmpManager snmpManager;
    private final SysDescrMapper sysDescrMapper;
    private final NetworkInterfaceService networkInterfaceService;
    private final DeviceDtoMapper deviceDtoMapper;

    public List<DeviceResponse> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(deviceDtoMapper::toResponse)
                .toList();
    }

    public DeviceResponse getDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
        return deviceDtoMapper.toResponse(device);
    }

    @Transactional
    public DeviceResponse syncDevice(DeviceRequest request) {
        String ipAddress = request.getIpAddress();

        String rawSoftware = snmpManager.getMetric(ipAddress, SnmpMetric.SYS_DESCR);
        String software = sysDescrMapper.map(rawSoftware);
        String name = snmpManager.getMetric(ipAddress, SnmpMetric.SYS_NAME);

        Optional<Device> existingOpt = deviceRepository.findByIpAddress(ipAddress);

        Device device;
        if (existingOpt.isPresent()) {
            device = existingOpt.get();
            device.setName(name);
            device.setSoftware(software);
        } else {
            device = Device.builder()
                    .ipAddress(ipAddress)
                    .name(name)
                    .software(software)
                    .build();
        }

        Device savedDevice = deviceRepository.save(device);

        try {
            List<VariableBinding> names = snmpManager.walk(ipAddress, SnmpMetric.IF_DESCR);
            List<VariableBinding> types = snmpManager.walk(ipAddress, SnmpMetric.IF_TYPE);
            List<VariableBinding> speeds = snmpManager.walk(ipAddress, SnmpMetric.IF_SPEED);
            networkInterfaceService.syncInterfaces(savedDevice, names, types, speeds);
        } catch (Exception e) {
            throw new SnmpServiceException("Failed to sync interfaces for " + ipAddress + ": " + e.getMessage());
        }

        return deviceDtoMapper.toResponse(savedDevice);
    }

    @Transactional
    public DeviceResponse updateDevice(Long id, DeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
        device.setIpAddress(request.getIpAddress());
        return deviceDtoMapper.toResponse(deviceRepository.save(device));
    }

    @Transactional
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
    }

    public List<Device> getAllDevicesEntities() {
        return deviceRepository.findAll();
    }
}