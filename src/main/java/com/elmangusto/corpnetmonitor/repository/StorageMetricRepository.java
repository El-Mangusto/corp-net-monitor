package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.StorageMetric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StorageMetricRepository extends JpaRepository<StorageMetric, Long> {

    @Query("SELECT s FROM StorageMetric s WHERE s.metric.device.id = :deviceId " +
            "ORDER BY s.metric.captureAt DESC")
    List<StorageMetric> findByDeviceIdOrderByCaptureAtDesc(
            @Param("deviceId") Long deviceId,
            Pageable pageable
    );

    @Query("SELECT s FROM StorageMetric s WHERE s.metric.device.id = :deviceId " +
            "AND s.metric.captureAt BETWEEN :from AND :to ORDER BY s.metric.captureAt DESC")
    List<StorageMetric> findByDeviceIdAndPeriod(
            @Param("deviceId") Long deviceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}
