package com.elmangusto.corpnetmonitor.repository;

import com.elmangusto.corpnetmonitor.model.StorageMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageMetricRepository extends JpaRepository<StorageMetric, Long> {
}
