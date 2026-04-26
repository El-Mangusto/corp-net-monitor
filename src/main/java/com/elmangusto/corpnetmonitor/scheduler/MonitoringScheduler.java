package com.elmangusto.corpnetmonitor.scheduler;

import com.elmangusto.corpnetmonitor.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoringScheduler {

    public static final int INTERVAL = 15000;
    private final MonitoringService monitoringService;

    @Scheduled(fixedRate = INTERVAL)
    public void scheduleMonitoring() {
        System.out.println(">>> [SCHEDULER] Запуск планового сканирования устройств...");
        try {
            monitoringService.scanDevices();
            System.out.println(">>> [SCHEDULER] Сканирование завершено успешно.");
        } catch (Exception e) {
            System.err.println(">>> [SCHEDULER] ОШИБКА при сканировании: " + e.getMessage());
        }
    }
}