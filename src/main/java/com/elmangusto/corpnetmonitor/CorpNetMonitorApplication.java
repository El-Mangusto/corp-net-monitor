package com.elmangusto.corpnetmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CorpNetMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorpNetMonitorApplication.class, args);
    }

}
