package com.elmangusto.corpnetmonitor.dto.mapper;

import com.elmangusto.corpnetmonitor.dto.StorageResponse;
import com.elmangusto.corpnetmonitor.model.Storage;
import org.springframework.stereotype.Component;

@Component
public class StorageDtoMapper {

    public StorageResponse toResponse(Storage storage) {
        return StorageResponse.builder()
                .id(storage.getId())
                .name(storage.getName())
                .type(storage.getType())
                .totalSizeGb(storage.getTotalSizeGb())
                .build();
    }
}