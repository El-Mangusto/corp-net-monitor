package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.dto.StorageResponse;
import com.elmangusto.corpnetmonitor.dto.mapper.StorageDtoMapper;
import com.elmangusto.corpnetmonitor.mapper.StorageMapper;
import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.Storage;
import com.elmangusto.corpnetmonitor.repository.DeviceRepository;
import com.elmangusto.corpnetmonitor.repository.StorageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final StorageDtoMapper storageDtoMapper;
    private final StorageMapper storageMapper;
    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public List<StorageResponse> getStoragesByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));

        return storageRepository.findByDevice(device).stream()
                .map(storageDtoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StorageResponse getStorage(Long deviceId, Long storageId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));

        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new EntityNotFoundException("Storage not found with id: " + storageId));

        if (!storage.getDevice().getId().equals(device.getId())) {
            throw new EntityNotFoundException("Storage " + storageId + " does not belong to device " + deviceId);
        }

        return storageDtoMapper.toResponse(storage);
    }

    @Transactional
    public void syncStorages(
            Device device,
            List<VariableBinding> names,
            List<VariableBinding> units,
            List<VariableBinding> sizes
    ) {
        List<Storage> scanned = storageMapper.mapStorages(names, units, sizes, device);

        for (Storage s : scanned) {
            Optional<Storage> existing = storageRepository.findByDeviceAndName(device, s.getName());

            if (existing.isPresent()) {
                Storage e = existing.get();
                e.setType(s.getType());
                e.setTotalSizeGb(s.getTotalSizeGb());
                storageRepository.save(e);
            } else {
                storageRepository.save(s);
            }
        }
    }
}