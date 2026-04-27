package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.dto.StorageMetricResponse;
import com.elmangusto.corpnetmonitor.dto.*;
import com.elmangusto.corpnetmonitor.dto.mapper.MetricDtoMapper;
import com.elmangusto.corpnetmonitor.model.Metric;
import com.elmangusto.corpnetmonitor.repository.MetricRepository;
import com.elmangusto.corpnetmonitor.repository.NetworkMetricRepository;
import com.elmangusto.corpnetmonitor.repository.StorageMetricRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricService {

    private static final int DEFAULT_LIMIT = 100;

    private final MetricRepository metricRepository;
    private final StorageMetricRepository storageMetricRepository;
    private final NetworkMetricRepository networkMetricRepository;
    private final MetricDtoMapper mapper;

    @Transactional(readOnly = true)
    public MetricLatestResponse getLatest(Long deviceId) {
        Metric metric = metricRepository.findTopByDeviceIdOrderByCaptureAtDesc(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("No metrics found for device: " + deviceId));
        return mapper.toLatestResponse(metric);
    }

    @Transactional(readOnly = true)
    public List<MetricHistoryResponse> getHistory(Long deviceId, LocalDateTime from, LocalDateTime to, Integer limit) {
        int pageSize = (limit != null) ? limit : DEFAULT_LIMIT;

        List<Metric> metrics = (from != null && to != null)
                ? metricRepository.findByDeviceIdAndPeriod(deviceId, from, to, PageRequest.of(0, pageSize))
                : metricRepository.findByDeviceIdOrderByCaptureAtDesc(deviceId, PageRequest.of(0, pageSize));

        return metrics.stream()
                .map(mapper::toHistoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StorageMetricResponse> getStorageHistory(Long deviceId, LocalDateTime from, LocalDateTime to, Integer limit) {
        int pageSize = (limit != null) ? limit : DEFAULT_LIMIT;

        return ((from != null && to != null)
                ? storageMetricRepository.findByDeviceIdAndPeriod(deviceId, from, to, PageRequest.of(0, pageSize))
                : storageMetricRepository.findByDeviceIdOrderByCaptureAtDesc(deviceId, PageRequest.of(0, pageSize)))
                .stream()
                .map(mapper::toStorageResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NetworkMetricResponse> getNetworkHistory(Long deviceId, LocalDateTime from, LocalDateTime to, Integer limit) {
        int pageSize = (limit != null) ? limit : DEFAULT_LIMIT;

        return ((from != null && to != null)
                ? networkMetricRepository.findByDeviceIdAndPeriod(deviceId, from, to, PageRequest.of(0, pageSize))
                : networkMetricRepository.findByDeviceIdOrderByCaptureAtDesc(deviceId, PageRequest.of(0, pageSize)))
                .stream()
                .map(mapper::toNetworkResponse)
                .toList();
    }
}