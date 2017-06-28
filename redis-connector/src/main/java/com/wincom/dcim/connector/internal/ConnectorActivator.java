package com.wincom.dcim.connector.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.connector.ConnectionFactory;

/**
 * Extension of the default OSGi bundle activator
 */
public final class ConnectorActivator
    implements BundleActivator
{
    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start( BundleContext bc )
        throws Exception
    {
        Dictionary<String, ?> props = new Hashtable<>();
        ServiceReference<NetworkService> serviceRef = bc.getServiceReference(NetworkService.class);
        NetworkService service = bc.getService(serviceRef);
        bc.registerService( ConnectionFactory.class.getName(), new ConnectionFactoryImpl(service), props );
    }

    public void stop( BundleContext bc )
        throws Exception
    {
    }
}

