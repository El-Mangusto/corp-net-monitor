package com.elmangusto.corpnetmonitor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString(exclude = {"storageMetrics", "networkMetrics"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "metrics")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_device")
    private Device device;

    @Column(name = "capture_at", precision = 0)
    private LocalDateTime captureAt;

    @Column
    private Long uptime;

    @Column
    private Integer processes;

    @Column(name = "cpu_load_avg")
    private Double cpuLoadAvg;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMetric> storageMetrics;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NetworkMetric> networkMetrics;
}
