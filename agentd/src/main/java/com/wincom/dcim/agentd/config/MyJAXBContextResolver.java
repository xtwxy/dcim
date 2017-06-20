package com.wincom.dcim.agentd.config;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import java.util.HashSet;





@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private HashSet<Class<?>> typeSet = new HashSet<Class<?>>();

	public MyJAXBContextResolver() throws Exception {
		Class<?>[] types = {
			};
		context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
		for (Class<?> type : types) {
			typeSet.add(type);
		}
	}

	public JAXBContext getContext(Class<?> objectType) {
		return (typeSet.contains(objectType)) ? context : null;
	}

}
