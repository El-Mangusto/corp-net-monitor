package com.elmangusto.corpnetmonitor.mapper;

import com.elmangusto.corpnetmonitor.exceptions.SnmpMapperException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UptimeMapper implements SnmpMapper<Long> {

    @Override
    public Long map(Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            return 0L;
        }

        String value = args[0].toString();

        if (value.isEmpty()) {
            return 0L;
        }

        try {
            long totalSeconds = 0;
            Pattern dayPattern = Pattern.compile("(\\d+)\\s+day");
            Matcher dayMatcher = dayPattern.matcher(value);
            if (dayMatcher.find()) {
                totalSeconds += Long.parseLong(dayMatcher.group(1)) * 24 * 3600;
            }

            Pattern timePattern = Pattern.compile("(\\d+):(\\d+):(\\d+)");
            Matcher matcher = timePattern.matcher(value);

            if (matcher.find()) {
                totalSeconds += Long.parseLong(matcher.group(1)) * 3600;
                totalSeconds += Long.parseLong(matcher.group(2)) * 60;
                totalSeconds += Long.parseLong(matcher.group(3));
            }
            return totalSeconds;
        } catch (Exception e) {
            throw new SnmpMapperException("UptimeMapper failed to parse value [" + value + "]: " + e.getMessage());
        }
    }
}