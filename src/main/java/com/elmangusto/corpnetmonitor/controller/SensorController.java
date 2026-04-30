package com.elmangusto.corpnetmonitor.controller;

import com.elmangusto.corpnetmonitor.dto.SensorDataRequest;
import com.elmangusto.corpnetmonitor.dto.SensorDataResponse;
import com.elmangusto.corpnetmonitor.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<SensorDataResponse> receive(
            @Valid @RequestBody SensorDataRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sensorService.receive(request));
    }

    @GetMapping("/{deviceId}/latest")
    public ResponseEntity<SensorDataResponse> getLatest(
            @PathVariable String deviceId) {
        return ResponseEntity.ok(sensorService.getLatest(deviceId));
    }

    @GetMapping
    public ResponseEntity<Collection<SensorDataResponse>> getAll() {
        return ResponseEntity.ok(sensorService.getAll());
    }
}