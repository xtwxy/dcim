package com.wincom.dcim.agentd.config;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.wincom.dcim.agentd.json.RequestConverter;
import com.wincom.dcim.agentd.json.SignalConverter;
import com.wincom.dcim.agentd.json.SignalConverterList;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import java.util.HashSet;

@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext context;
    private HashSet<Class<?>> typeSet = new HashSet<Class<?>>();

    public MyJAXBContextResolver() {
        Class<?>[] types = {
                RequestConverter.class,
                SignalConverter.class,
                SignalConverterList.class
        };
        try {
            context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        for (Class<?> type : types) {
            typeSet.add(type);
        }
    }

    public JAXBContext getContext(Class<?> objectType) {
        return (typeSet.contains(objectType)) ? context : null;
    }

}
