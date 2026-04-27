package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.exceptions.SnmpMapperException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UptimeMapper {

    private static final Pattern DAY_PATTERN  = Pattern.compile("(\\d+)\\s+day");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+):(\\d+):(\\d+)");

    public Long map(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }

        try {
            long totalSeconds = 0;

            Matcher dayMatcher = DAY_PATTERN.matcher(value);
            if (dayMatcher.find()) {
                totalSeconds += Long.parseLong(dayMatcher.group(1)) * 24 * 3600;
            }

            Matcher timeMatcher = TIME_PATTERN.matcher(value);
            if (timeMatcher.find()) {
                totalSeconds += Long.parseLong(timeMatcher.group(1)) * 3600;
                totalSeconds += Long.parseLong(timeMatcher.group(2)) * 60;
                totalSeconds += Long.parseLong(timeMatcher.group(3));
            }

            return totalSeconds;

        } catch (Exception e) {
            throw new SnmpMapperException(
                    "UptimeMapper failed to parse value [" + value + "]: " + e.getMessage());
        }
    }
}