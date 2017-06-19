package com.wincom.dcim.connector.internal;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.connector.ConnectorService;

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
        System.out.println( "STARTING com.wincom.dcim.connector" );

        Dictionary<String, ?> props = new Hashtable<>();
        // add specific service properties here...

        System.out.println( "REGISTER com.wincom.dcim.connector.ConnectorService" );

        // Register our example service implementation in the OSGi service registry
        bc.registerService( ConnectorService.class.getName(), new ConnectorServiceImpl(), props );
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop( BundleContext bc )
        throws Exception
    {
        System.out.println( "STOPPING com.wincom.dcim.connector" );

        // no need to unregister our service - the OSGi framework handles it for us
    }
}

