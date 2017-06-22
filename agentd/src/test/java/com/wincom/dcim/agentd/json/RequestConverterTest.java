package com.wincom.dcim.agentd.json;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;
import com.wincom.dcim.agentd.domain.*;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.SetSignalValues;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by master on 6/22/17.
 */
public class RequestConverterTest {
    @Test
    public void testMashallGetRequest() throws JAXBException {
        Set<String> keys = new HashSet<>();
        for(int i = 0; i < 10; ++i) {
            keys.add("key" + i);
        }
        GetSignalValues.Request request = new GetSignalValues.Request(null, keys);
        RequestConverter converter = new RequestConverter(request);

        JAXBContext context = JAXBContext.newInstance(RequestConverter.class);
        Marshaller m = context.createMarshaller();
        JSONMarshaller marshaller = JSONJAXBContext.getJSONMarshaller(m, context);
        marshaller.marshallToJSON(converter, System.out);
        System.out.println();
    }

    @Test
    public void testUnMarshalGetRequest() throws JAXBException {
        String json = "{\"type\":\"GET_SIGNAL_VALUES_REQUEST\"," +
                "\"keys\":[\"key1\",\"key2\",\"key0\",\"key5\",\"key6\",\"key3\",\"key4\",\"key9\",\"key7\",\"key8\"]}";

        JAXBContext context = JAXBContext.newInstance(RequestConverter.class);
        Unmarshaller um = context.createUnmarshaller();
        JSONUnmarshaller unmarshaller = JSONJAXBContext.getJSONUnmarshaller(um, context);

        InputStream is = new ByteArrayInputStream(json.getBytes());
        RequestConverter converter = unmarshaller.unmarshalFromJSON(is, RequestConverter.class);
        System.out.println("request: " + converter.getRequest(null));
    }

    @Test
    public void testMarshalSetRequest() throws JAXBException {
        Map<String, Signal> signalMap = new HashMap<>();

        for (int i = 0; i < 3; ++i) {
            signalMap.put("di-" + i, new DigitalSignal(i % 2 == 0));
            signalMap.put("ii-" + i, new IntegerSignal(i));
            signalMap.put("ai-" + i, new AnalogSignal(100.0 + i));
            signalMap.put("si-" + i, new StringSignal("" + 100.0 + i));
            signalMap.put("ti-" + i, new TimstampSignal(new Date()));
        }

        SetSignalValues.Request request = new SetSignalValues.Request(null, signalMap);
        RequestConverter converter = new RequestConverter(request);

        JAXBContext context = JAXBContext.newInstance(RequestConverter.class);
        Marshaller m = context.createMarshaller();
        JSONMarshaller marshaller = JSONJAXBContext.getJSONMarshaller(m, context);
        marshaller.marshallToJSON(converter, System.out);
        System.out.println();
    }

    @Test
    public void testUnMarshalSetRequest() throws JAXBException {
        String json = "{\"type\":\"SET_SIGNAL_VALUES_REQUEST\"," +
                "\"signals\":[" +
                "{\"type\":\"ANALOG\",\"value\":\"102.0\",\"key\":\"ai-2\"}," +
                "{\"type\":\"ANALOG\",\"value\":\"101.0\",\"key\":\"ai-1\"}," +
                "{\"type\":\"DIGITAL\",\"value\":\"true\",\"key\":\"di-2\"}," +
                "{\"type\":\"ANALOG\",\"value\":\"100.0\",\"key\":\"ai-0\"}," +
                "{\"type\":\"DIGITAL\",\"value\":\"false\",\"key\":\"di-1\"}," +
                "{\"type\":\"DIGITAL\",\"value\":\"true\",\"key\":\"di-0\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.00\",\"key\":\"si-0\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-22 15:31:11.778\",\"key\":\"ti-2\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-22 15:31:11.778\",\"key\":\"ti-0\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.02\",\"key\":\"si-2\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.01\",\"key\":\"si-1\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-22 15:31:11.778\",\"key\":\"ti-1\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"0\",\"key\":\"ii-0\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"2\",\"key\":\"ii-2\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"1\",\"key\":\"ii-1\"}]" +
                "}\n";

        JAXBContext context = JAXBContext.newInstance(RequestConverter.class);
        Unmarshaller um = context.createUnmarshaller();
        JSONUnmarshaller unmarshaller = JSONJAXBContext.getJSONUnmarshaller(um, context);

        InputStream is = new ByteArrayInputStream(json.getBytes());
        RequestConverter converter = unmarshaller.unmarshalFromJSON(is, RequestConverter.class);
        System.out.println("request: " + converter.getRequest(null));
    }
}
