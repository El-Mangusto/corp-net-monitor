package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByIpAddress(String ipAddress);
}

