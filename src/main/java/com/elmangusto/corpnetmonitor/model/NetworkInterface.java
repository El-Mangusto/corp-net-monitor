package com.elmangusto.corpnetmonitor.model;

import com.elmangusto.corpnetmonitor.model.enums.InterfaceType;
import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = "device")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "network_interfaces")
public class NetworkInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "if_index", nullable = false)
    private Integer ifIndex;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private InterfaceType type;

    @Column(name = "speed_bps")
    private Long speedBps;

    @Column(name = "last_in_octets")
    private Long lastInOctets = 0L;

    @Column(name = "last_out_octets")
    private Long lastOutOctets = 0L;
}
