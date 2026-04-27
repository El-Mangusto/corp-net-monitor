package com.elmangusto.corpnetmonitor.controller;

import com.elmangusto.corpnetmonitor.dto.NetworkInterfaceResponse;
import com.elmangusto.corpnetmonitor.service.NetworkInterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices/{deviceId}/interfaces")
@RequiredArgsConstructor
public class NetworkInterfaceController {

    private final NetworkInterfaceService networkInterfaceService;

    @GetMapping
    public ResponseEntity<List<NetworkInterfaceResponse>> getInterfacesByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(networkInterfaceService.getInterfacesByDevice(deviceId));
    }

    @GetMapping("/{interfaceId}")
    public ResponseEntity<NetworkInterfaceResponse> getInterface(
            @PathVariable Long deviceId,
            @PathVariable Long interfaceId
    ) {
        return ResponseEntity.ok(networkInterfaceService.getInterface(deviceId, interfaceId));
    }
}