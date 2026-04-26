package com.elmangusto.corpnetmonitor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "network_metrics")
public class NetworkMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interface_id", nullable = false)
    private NetworkInterface networkInterface;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterfaceStatus status;

    @Column(name = "in_speed_kbps")
    private Long inSpeedKbps;

    @Column(name = "out_speed_kbps")
    private Long outSpeedKbps;

    @Column(name = "in_utilization")
    private Double inUtilization;

    @Column(name = "out_utilization")
    private Double outUtilization;
}
