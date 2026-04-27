package com.elmangusto.corpnetmonitor;

import com.elmangusto.corpnetmonitor.collector.SnmpManager;
import com.elmangusto.corpnetmonitor.collector.SnmpMetric;
import org.snmp4j.smi.VariableBinding;
import java.util.List;

public class NetworkWalkTest {
    public static void main(String[] args) {
        try {
            SnmpManager manager = new SnmpManager();
            String ip = "127.0.0.1"; // Твоя виртуалка или 127.0.0.1 для хоста

            System.out.println("=== РАСШИРЕННЫЕ СЕТЕВЫЕ МЕТРИКИ (КЛОД-ЭДИШН) ===");

            // 1. Названия (чтобы понять, какой индекс к чему относится)
            System.out.println("\n--- Interface Description (IF_DESCR) ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_DESCR));

            // 2. Тип интерфейса (чтобы найти лупбеки)
            System.out.println("\n--- Interface Type (6=Ethernet, 24=Loopback, 71=Wi-Fi) ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_TYPE));

            // 3. Максимальная скорость (IF_SPEED)
            System.out.println("\n--- Max Speed bps (IF_SPEED) ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_SPEED));

            // 4. 64-битные счетчики входящего трафика (HC_IN)
            System.out.println("\n--- 64-bit Traffic IN (IF_HC_IN) ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_IN_OCTETS));

            // 5. 64-битные счетчики исходящего трафика (HC_OUT)
            System.out.println("\n--- 64-bit Traffic OUT (IF_HC_OUT) ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_OUT_OCTETS));

            manager.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printWalk(List<VariableBinding> results) {
        if (results.isEmpty()) {
            System.out.println("НЕТ ДАННЫХ (возможно, устройство не поддерживает 64-битные счетчики)");
            return;
        }
        for (VariableBinding vb : results) {
            System.out.println("OID: " + vb.getOid() + " | Value: " + vb.getVariable());
        }
    }
}