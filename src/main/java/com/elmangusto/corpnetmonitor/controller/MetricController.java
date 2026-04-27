package com.elmangusto.corpnetmonitor.controller;

import com.elmangusto.corpnetmonitor.dto.*;
import com.elmangusto.corpnetmonitor.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/devices/{deviceId}/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @GetMapping("/latest")
    public ResponseEntity<MetricLatestResponse> getLatest(@PathVariable Long deviceId) {
        return ResponseEntity.ok(metricService.getLatest(deviceId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<MetricHistoryResponse>> getHistory(
            @PathVariable Long deviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(metricService.getHistory(deviceId, from, to, limit));
    }

    @GetMapping("/storage/history")
    public ResponseEntity<List<StorageMetricResponse>> getStorageHistory(
            @PathVariable Long deviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(metricService.getStorageHistory(deviceId, from, to, limit));
    }

    @GetMapping("/network/history")
    public ResponseEntity<List<NetworkMetricResponse>> getNetworkHistory(
            @PathVariable Long deviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(metricService.getNetworkHistory(deviceId, from, to, limit));
    }
}