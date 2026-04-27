package com.elmangusto.corpnetmonitor;

import com.elmangusto.corpnetmonitor.collector.SnmpManager;
import com.elmangusto.corpnetmonitor.collector.SnmpMetric;
import org.snmp4j.smi.VariableBinding;
import java.util.List;

public class WalkTest {
    public static void main(String[] args) {
        try {
            SnmpManager manager = new SnmpManager();
            String ip = "127.0.0.1";

            System.out.println("=== ТОЛЬКО ДАННЫЕ ХРАНИЛИЩ (СЫРЫЕ) ===");

            // 1. Собираем названия
            System.out.println("\n--- " + SnmpMetric.IF_TYPE.getName() + " ---");
            printWalk(manager.walk(ip, SnmpMetric.IF_TYPE));

            // 2. Собираем полный размер (в блоках)
            System.out.println("\n--- " + SnmpMetric.HR_STORAGE_SIZE.getName() + " ---");
            printWalk(manager.walk(ip, SnmpMetric.HR_STORAGE_SIZE));

            // 3. Собираем занятое место (в блоках)
            System.out.println("\n--- " + SnmpMetric.HR_STORAGE_USED.getName() + " ---");
            printWalk(manager.walk(ip, SnmpMetric.HR_STORAGE_USED));

            manager.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printWalk(List<VariableBinding> results) {
        for (VariableBinding vb : results) {
            System.out.println("OID: " + vb.getOid() + " | Value: " + vb.getVariable());
        }
    }
}