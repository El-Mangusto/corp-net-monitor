package com.elmangusto.corpnetmonitor.collector;

import com.elmangusto.corpnetmonitor.exceptions.SnmpManagerException;
import jakarta.annotation.PreDestroy;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SnmpManager {

    private final Snmp snmp;

    public SnmpManager() throws IOException {
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        this.snmp = new Snmp(transport);
        this.snmp.listen();
    }

    public String getMetric(String ipAddress, SnmpMetric metric) {
        try {
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public"));
            target.setAddress(GenericAddress.parse("udp:" + ipAddress + "/161"));
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(metric.getOid())));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);

            if (event != null && event.getResponse() != null) {
                PDU responsePdu = event.getResponse();

                if (responsePdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
                    return responsePdu.getVariableBindings().get(0).getVariable().toString();
                } else {
                    throw new SnmpManagerException("SNMP Error: " + responsePdu.getErrorStatusText());
                }
            } else {
                throw new SnmpManagerException("Timeout: No response from " + ipAddress);
            }

        } catch (IOException e) {
            throw new SnmpManagerException("Network I/O Error: " + e.getMessage());
        }
    }

    public List<VariableBinding> walk(String ipAddress, SnmpMetric metric) {
        List<VariableBinding> result = new ArrayList<>();

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(GenericAddress.parse("udp:" + ipAddress + "/161"));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(metric.getOid()));

        if (events == null || events.isEmpty()) {
            throw new SnmpManagerException("No data received during WALK from " + ipAddress);
        }

        for (TreeEvent event : events) {
            if (event == null) continue;

            if (event.isError()) {
                throw new SnmpManagerException("SNMP WALK Error: " + event.getErrorMessage());
            }

            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings != null) {
                for (VariableBinding vb : varBindings) {
                    result.add(vb);
                }
            }
        }
        return result;
    }

    @PreDestroy
    public void stop() throws IOException {
        if (snmp != null) {
            snmp.close();
        }
    }
}
