package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    Optional<Storage> findByDeviceAndName(Device device, String name);

    List<Storage> findByDevice(Device device);
}