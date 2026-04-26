package com.elmangusto.corpnetmonitor.collector;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnmpMetric {

    SYS_DESCR("1.3.6.1.2.1.1.1.0", "System Descr", "String", "GET"),
    SYS_UPTIME("1.3.6.1.2.1.1.3.0", "Uptime System", "TimeTicks", "GET"),
    SYS_NAME("1.3.6.1.2.1.1.5.0", "System Name", "String", "GET"),


    HR_SYSTEM_PROCESSES("1.3.6.1.2.1.25.1.6.0", "System Processes", "Count", "GET"),
    HR_PROCESSOR_LOAD("1.3.6.1.2.1.25.3.3.1.2", "CPU Load", "Percentage", "WALK"),
    HR_STORAGE_DESCR("1.3.6.1.2.1.25.2.3.1.3", "Storage Name", "String", "WALK"),
    HR_STORAGE_USED("1.3.6.1.2.1.25.2.3.1.6", "Storage Used", "Blocks", "WALK"),
    HR_STORAGE_SIZE("1.3.6.1.2.1.25.2.3.1.5", "Storage Size", "Blocks", "WALK"),
    HR_STORAGE_UNITS("1.3.6.1.2.1.25.2.3.1.4", "Storage Units", "Units", "WALK"),


    IF_DESCR("1.3.6.1.2.1.2.2.1.2", "Interface Name", "String", "WALK"),
    IF_TYPE("1.3.6.1.2.1.2.2.1.3", "Interface Type", "Integer", "WALK"),
    IF_SPEED("1.3.6.1.2.1.2.2.1.5", "Interface Speed", "Bps", "WALK"),
    IF_OPER_STATUS("1.3.6.1.2.1.2.2.1.8", "Interface Status", "Integer", "WALK"),
    IF_IN_OCTETS("1.3.6.1.2.1.2.2.1.10", "Traffic In", "Bytes", "WALK"),
    IF_OUT_OCTETS("1.3.6.1.2.1.2.2.1.16", "Traffic Out", "Bytes", "WALK");


    private final String oid;
    private final String name;
    private final String unit;
    private final String method;
}
