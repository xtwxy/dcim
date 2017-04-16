package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;

public final class AgentdServiceActivator implements BundleActivator {

	public void start(BundleContext bc) throws Exception {
		Dictionary<String, ?> props = new Hashtable<>();

		bc.registerService(AgentdService.class.getName(), new AgentdServiceImpl(), props);
	}

	public void stop(BundleContext bc) throws Exception {

	}
}
