package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.NetworkMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkMetricRepository extends JpaRepository<NetworkMetric, Long> {
}
