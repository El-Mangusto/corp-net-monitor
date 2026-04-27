package com.elmangusto.corpnetmonitor.controller;

import com.elmangusto.corpnetmonitor.dto.DeviceRequest;
import com.elmangusto.corpnetmonitor.dto.DeviceResponse;
import com.elmangusto.corpnetmonitor.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDevice(id));
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> addDevice(@Valid @RequestBody DeviceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.syncDevice(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequest request
    ) {
        return ResponseEntity.ok(deviceService.updateDevice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}