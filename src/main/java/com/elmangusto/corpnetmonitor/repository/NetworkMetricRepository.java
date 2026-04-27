package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.NetworkMetric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NetworkMetricRepository extends JpaRepository<NetworkMetric, Long> {

    @Query("SELECT n FROM NetworkMetric n WHERE n.metric.device.id = :deviceId " +
            "ORDER BY n.metric.captureAt DESC")
    List<NetworkMetric> findByDeviceIdOrderByCaptureAtDesc(
            @Param("deviceId") Long deviceId,
            Pageable pageable
    );

    @Query("SELECT n FROM NetworkMetric n WHERE n.metric.device.id = :deviceId " +
            "AND n.metric.captureAt BETWEEN :from AND :to ORDER BY n.metric.captureAt DESC")
    List<NetworkMetric> findByDeviceIdAndPeriod(
            @Param("deviceId") Long deviceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}
