package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class MP3000Activator
        implements BundleActivator {

    @Override
    public void start(BundleContext bc)
            throws Exception {
        ServiceReference<AgentdService> serviceRef = bc.getServiceReference(AgentdService.class);
        AgentdService service = bc.getService(serviceRef);
        CodecFactory factory = new MP3000CodecFactoryImpl();
        service.registerCodecFactory("MP3000", factory);
    }

    @Override
    public void stop(BundleContext bc)
            throws Exception {
    }
}
