package com.elmangusto.corpnetmonitor.dto.mapper;

import com.elmangusto.corpnetmonitor.dto.*;
import com.elmangusto.corpnetmonitor.model.Metric;
import com.elmangusto.corpnetmonitor.model.NetworkMetric;
import com.elmangusto.corpnetmonitor.model.StorageMetric;
import org.springframework.stereotype.Component;

@Component
public class MetricDtoMapper {

    public MetricLatestResponse toLatestResponse(Metric metric) {
        return MetricLatestResponse.builder()
                .captureAt(metric.getCaptureAt())
                .uptime(metric.getUptime())
                .processes(metric.getProcesses())
                .cpuLoadAvg(metric.getCpuLoadAvg())
                .storages(metric.getStorageMetrics().stream()
                        .map(this::toStorageResponse)
                        .toList())
                .network(metric.getNetworkMetrics().stream()
                        .map(this::toNetworkResponse)
                        .toList())
                .build();
    }

    public MetricHistoryResponse toHistoryResponse(Metric metric) {
        return MetricHistoryResponse.builder()
                .captureAt(metric.getCaptureAt())
                .uptime(metric.getUptime())
                .processes(metric.getProcesses())
                .cpuLoadAvg(metric.getCpuLoadAvg())
                .build();
    }

    public StorageMetricResponse toStorageResponse(StorageMetric s) {
        return StorageMetricResponse.builder()
                .name(s.getName())
                .type(s.getType())
                .totalSizeGb(s.getTotalSizeGb())
                .usedSizeGb(s.getUsedSizeGb())
                .usedPercent(s.getUsedPercent())
                .captureAt(s.getMetric().getCaptureAt())
                .build();
    }

    public NetworkMetricResponse toNetworkResponse(NetworkMetric n) {
        return NetworkMetricResponse.builder()
                .interfaceName(n.getNetworkInterface().getName())
                .status(n.getStatus())
                .inSpeedKbps(n.getInSpeedKbps())
                .outSpeedKbps(n.getOutSpeedKbps())
                .inUtilization(n.getInUtilization())
                .outUtilization(n.getOutUtilization())
                .captureAt(n.getMetric().getCaptureAt())
                .build();
    }
}
