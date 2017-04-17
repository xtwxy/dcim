package com.wincom.dcim.agentd.internal;

import static java.lang.System.out;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class ServiceListenerImpl implements ServiceListener {

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
                out.println("REGISTERED: " + event.getServiceReference().toString());
                break;
            case ServiceEvent.MODIFIED:
                out.println("MODIFIED: " + event.getServiceReference().toString());
                break;
            case ServiceEvent.UNREGISTERING:
                out.println("UNREGISTERING: " + event.getServiceReference().toString());
                break;
            case ServiceEvent.MODIFIED_ENDMATCH:
                out.println("MODIFIED_ENDMATCH: " + event.getServiceReference().toString());
                break;
            default:
                out.println("Unknown ServiceEvent: " + event.getServiceReference().toString());
                break;
        }
    }
}
