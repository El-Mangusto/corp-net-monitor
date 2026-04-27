package com.elmangusto.corpnetmonitor.model;

import com.elmangusto.corpnetmonitor.model.enums.InterfaceStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = {"metric", "networkInterface"})
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
