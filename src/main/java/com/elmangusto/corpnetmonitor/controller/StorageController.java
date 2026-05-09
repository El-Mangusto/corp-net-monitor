package com.elmangusto.corpnetmonitor.controller;

import com.elmangusto.corpnetmonitor.dto.StorageResponse;
import com.elmangusto.corpnetmonitor.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices/{deviceId}/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<StorageResponse>> getStoragesByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(storageService.getStoragesByDevice(deviceId));
    }

    @GetMapping("/{storageId}")
    public ResponseEntity<StorageResponse> getStorage(
            @PathVariable Long deviceId,
            @PathVariable Long storageId
    ) {
        return ResponseEntity.ok(storageService.getStorage(deviceId, storageId));
    }
}