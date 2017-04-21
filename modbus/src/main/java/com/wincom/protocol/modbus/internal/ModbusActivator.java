package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class ModbusActivator
        implements BundleActivator {

    @Override
    public void start(BundleContext bc)
            throws Exception {
        ServiceReference<AgentdService> serviceRef = bc.getServiceReference(AgentdService.class);
        AgentdService service = bc.getService(serviceRef);
        CodecFactory factory = new ModbusCodecFactoryImpl(service);
        service.registerCodecFactory("modbus", factory);
    }

    @Override
    public void stop(BundleContext bc)
            throws Exception {
    }
}
