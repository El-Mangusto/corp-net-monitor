package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.Device;
import com.elmangusto.corpnetmonitor.model.NetworkInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkInterfaceRepository extends JpaRepository<NetworkInterface, Long> {

    Optional<NetworkInterface> findByDeviceAndIfIndex(Device device, Integer ifIndex);

    List<NetworkInterface> findByDevice(Device device);
}

