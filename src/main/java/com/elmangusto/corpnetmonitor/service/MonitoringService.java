package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.collector.SnmpManager;
import com.elmangusto.corpnetmonitor.exceptions.SnmpServiceException;
import com.elmangusto.corpnetmonitor.mapper.*;
import com.elmangusto.corpnetmonitor.model.*;
import com.elmangusto.corpnetmonitor.collector.SnmpMetric;
import com.elmangusto.corpnetmonitor.repository.MetricRepository;
import com.elmangusto.corpnetmonitor.repository.NetworkInterfaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final SnmpManager snmpManager;
    private final UptimeMapper uptimeMapper;
    private final StorageMapper storageMapper;
    private final CpuLoadMapper cpuLoadMapper;
    private final ProcessesMapper processesMapper;
    private final MetricRepository metricRepository;
    private final DeviceService deviceService;
    private final NetworkMetricMapper networkMetricMapper;
    private final NetworkInterfaceRepository networkInterfaceRepository;

    public void scanDevices() {
        deviceService.getAllDevices().forEach(this::collectDeviceMetric);
    }

    @Transactional
    public void collectDeviceMetric(Device device) {
        try {
            Metric metric = Metric.builder()
                    .device(device)
                    .captureAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .storageMetrics(new ArrayList<>())
                    .networkMetrics(new ArrayList<>())
                    .build();

            collectUptime(device.getIpAddress(), metric);
            collectProcesses(device.getIpAddress(), metric);
            collectCpuLoad(device.getIpAddress(), metric);
            collectStorages(device.getIpAddress(), metric);
            collectNetworkMetrics(device, metric);

            metricRepository.save(metric);

        } catch (Exception e) {
            throw new SnmpServiceException("Error monitoring device " + device.getIpAddress() + ": " + e.getMessage());
        }
    }

    private void collectUptime(String ip, Metric metric) {
        String rawUptime = snmpManager.getMetric(ip, SnmpMetric.SYS_UPTIME);
        metric.setUptime(uptimeMapper.map(rawUptime));
    }

    private void collectProcesses(String ip, Metric metric) {
        String rawProcesses = snmpManager.getMetric(ip, SnmpMetric.HR_SYSTEM_PROCESSES);
        metric.setProcesses(processesMapper.map(rawProcesses));
    }
    private void collectCpuLoad(String ip, Metric metric) {
        var rawCpuLoad = snmpManager.walk(ip, SnmpMetric.HR_PROCESSOR_LOAD);
        metric.setCpuLoadAvg(cpuLoadMapper.map(rawCpuLoad));
    }

    private void collectStorages(String ip, Metric metric) {
        var names = snmpManager.walk(ip, SnmpMetric.HR_STORAGE_DESCR);
        var units = snmpManager.walk(ip, SnmpMetric.HR_STORAGE_UNITS);
        var sizes = snmpManager.walk(ip, SnmpMetric.HR_STORAGE_SIZE);
        var used = snmpManager.walk(ip, SnmpMetric.HR_STORAGE_USED);

        List<StorageMetric> storageList = storageMapper.map(names, units, sizes, used, metric);
        metric.getStorageMetrics().addAll(storageList);
    }

    private void collectNetworkMetrics(Device device, Metric metric) {
        String ip = device.getIpAddress();

        var inOctets = snmpManager.walk(ip, SnmpMetric.IF_IN_OCTETS);
        var outOctets = snmpManager.walk(ip, SnmpMetric.IF_OUT_OCTETS);
        var statuses = snmpManager.walk(ip, SnmpMetric.IF_OPER_STATUS);

        List<NetworkInterface> interfaces = networkInterfaceRepository.findByDevice(device);

        List<NetworkMetric> netList = networkMetricMapper.map(
                inOctets,
                outOctets,
                statuses,
                metric,
                interfaces
        );

        metric.getNetworkMetrics().addAll(netList);

        networkInterfaceRepository.saveAll(interfaces);
    }
}
