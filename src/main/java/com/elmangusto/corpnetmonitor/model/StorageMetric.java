package com.elmangusto.corpnetmonitor.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = {"metric", "storage"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "storage_metrics")
public class StorageMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metric", nullable = false)
    private Metric metric;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @Column(name = "used_size_gb")
    private Double usedSizeGb;

    @Column(name = "used_percent")
    private Double usedPercent;
}