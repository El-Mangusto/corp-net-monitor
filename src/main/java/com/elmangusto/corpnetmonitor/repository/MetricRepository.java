package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.Metric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

    Optional<Metric> findTopByDeviceIdOrderByCaptureAtDesc(Long deviceId);

    List<Metric> findByDeviceIdOrderByCaptureAtDesc(Long deviceId, Pageable pageable);

    @Query("SELECT m FROM Metric m WHERE m.device.id = :deviceId " +
            "AND m.captureAt BETWEEN :from AND :to ORDER BY m.captureAt DESC")
    List<Metric> findByDeviceIdAndPeriod(
            @Param("deviceId") Long deviceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}
