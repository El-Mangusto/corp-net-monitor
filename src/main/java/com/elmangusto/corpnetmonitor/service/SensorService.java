package com.elmangusto.corpnetmonitor.service;

import com.elmangusto.corpnetmonitor.dto.SensorDataRequest;
import com.elmangusto.corpnetmonitor.dto.SensorDataResponse;
import com.elmangusto.corpnetmonitor.dto.mapper.SensorDataMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final ConcurrentHashMap<String, SensorDataResponse> store = new ConcurrentHashMap<>();

    private final SensorDataMapper mapper;

    public SensorDataResponse receive(SensorDataRequest request) {
        SensorDataResponse response = mapper.toResponse(request);
        store.put(request.getDeviceId(), response);
        return response;
    }

    public SensorDataResponse getLatest(String deviceId) {
        SensorDataResponse data = store.get(deviceId);
        if (data == null) {
            throw new EntityNotFoundException("No data received from sensor: " + deviceId);
        }
        return data;
    }

    public Collection<SensorDataResponse> getAll() {
        return store.values();
    }
}