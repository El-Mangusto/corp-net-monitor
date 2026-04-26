package com.elmangusto.corpnetmonitor;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpTest {
    public static void main(String[] args) throws Exception {

        // 1. Создаем транспортный уровень (UDP)
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen(); // Начинаем слушать ответы

        // 2. Описываем цель (куда отправляем запрос)
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public")); // Пароль доступа
        target.setAddress(GenericAddress.parse("udp:127.0.0.1/161")); // IP и порт
        target.setRetries(2); // Попытки при неудаче
        target.setTimeout(1500); // Ожидание ответа в мс
        target.setVersion(SnmpConstants.version2c); // Версия протокола

        // 3. Создаем "конверт" запроса (PDU)
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.25.1.6.0"))); // OID системного аптайма
        pdu.setType(PDU.GET); // Тип операции - получить значение

        // 4. Отправляем запрос и получаем ответ
        ResponseEvent response = snmp.send(pdu, target);

        // 5. Обрабатываем результат
        if (response != null && response.getResponse() != null) {
            PDU responsePDU = response.getResponse();
            String uptime = responsePDU.getVariableBindings().get(0).getVariable().toString();
            System.out.println("Системный аптайм: " + uptime);
        } else {
            System.out.println("Ошибка: Устройство не ответило (проверь службу SNMP)");
        }

        snmp.close(); // Закрываем соединение
    }
}
