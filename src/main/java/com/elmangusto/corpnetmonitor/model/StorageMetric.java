package com.elmangusto.corpnetmonitor.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@ToString(exclude = "metric")
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

    @Column
    private String name;

    @Column
    private String type;

    @Column(name = "total_size_gb")
    private Double totalSizeGb;

    @Column(name = "used_size_gb")
    private Double usedSizeGb;

    @Column(name = "used_percent")
    private Double usedPercent;
}
