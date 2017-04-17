package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.ClientChannelFactory;
import com.wincom.dcim.agentd.Connector;
import org.osgi.framework.BundleContext;

public class ClientChannelFactoryImpl implements ClientChannelFactory {

    private BundleContext bundleContext;

    public ClientChannelFactoryImpl(BundleContext context) {
        this.bundleContext = context;
    }

    @Override
    public void create(String host, int port, Connector connector) {
    }

}
