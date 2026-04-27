package com.elmangusto.corpnetmonitor.mapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UptimeMapperTest {

    private final UptimeMapper uptimeMapper = new UptimeMapper();

    @Test
    void map() {
        String valueUptime = "2 days, 0:45:39.09";

        Long actualResult = uptimeMapper.map(valueUptime);

        Long expectedResult = 175539L;

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}