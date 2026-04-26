package com.elmangusto.corpnetmonitor.mapper;

public interface SnmpMapper<T> {

    T map(Object... args);
}
