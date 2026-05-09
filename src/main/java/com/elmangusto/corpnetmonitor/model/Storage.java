package com.elmangusto.corpnetmonitor.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = "device")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "storages")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    private String name;

    @Column
    private String type;

    @Column(name = "total_size_gb")
    private Double totalSizeGb;
}