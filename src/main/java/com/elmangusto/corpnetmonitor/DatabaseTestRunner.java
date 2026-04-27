//package com.elmangusto.corpnetmonitor;
//
//import com.elmangusto.corpnetmonitor.model.Device;
//import com.elmangusto.corpnetmonitor.service.DeviceService;
//import com.elmangusto.corpnetmonitor.service.MonitoringService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DatabaseTestRunner implements CommandLineRunner {
//
//    private final MonitoringService monitoringService;
//    private final DeviceService deviceService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println(">>> [START] ЗАПУСК ТЕСТОВОГО СБОРА ДАННЫХ...");
//
//        String ip = "127.0.0.1";
//
//        try {
//            // 1. Синхронизируем устройство.
//            // Теперь это создаст/обновит девайс И его интерфейсы автоматически.
//            System.out.println(">>> Шаг 1: Синхронизация устройства и интерфейсов (" + ip + ")");
//            Device myPc = deviceService.syncDevice(ip);
//
//            // 2. Запускаем сбор динамических метрик (Uptime, CPU, Storage, и скоро Network)
//            System.out.println(">>> Шаг 2: Опрос динамических метрик для: " + myPc.getName());
//            monitoringService.collectDeviceMetric(myPc);
//
//            System.out.println(">>> [SUCCESS] ТЕСТ ЗАВЕРШЕН УСПЕШНО!");
//
//        } catch (Exception e) {
//            System.err.println(">>> [ERROR] ОШИБКА В ПРОЦЕССЕ ТЕСТА: " + e.getMessage());
//            // e.printStackTrace(); // Раскомментируй, если нужно видеть весь стек ошибки
//        }
//    }
//}