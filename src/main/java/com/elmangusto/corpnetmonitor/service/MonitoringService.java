package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final DeviceRepository deviceRepository;
    private final MonitoringCollector monitoringCollector;

    public void scanDevices() {
        deviceRepository.findAll().forEach(monitoringCollector::collectDeviceMetric);
    }
}