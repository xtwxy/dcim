package com.wincom.dcim.agentd.json;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;
import com.wincom.dcim.agentd.domain.*;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by master on 6/21/17.
 */
public class SignalConverterMarshalTest {
    @Test
    public void testMarshal() throws Exception {
        JAXBContext context = JAXBContext.newInstance(SignalConverter.class);
        Marshaller m = context.createMarshaller();
        JSONMarshaller marshaller = JSONJAXBContext.getJSONMarshaller(m, context);
        marshaller.setProperty(JSONMarshaller.FORMATTED, true);
        AnalogSignal signal = new AnalogSignal(100.0);
        marshaller.marshallToJSON(new SignalConverter(signal), System.out);
        System.out.println();
    }

    @Test
    public void testMarshalList() throws Exception {
        JAXBContext context = JAXBContext.newInstance(new Class[]{
                SignalConverter.class,
                SignalConverterList.class
        });
        Marshaller m = context.createMarshaller();
        JSONMarshaller marshaller = JSONJAXBContext.getJSONMarshaller(m, context);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        List<SignalConverter> l = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            l.add(new SignalConverter(new DigitalSignal(i % 2 == 0)));
            l.add(new SignalConverter(new IntegerSignal(i)));
            l.add(new SignalConverter(new AnalogSignal(100.0 + i)));
            l.add(new SignalConverter(new StringSignal("" + 100.0 + i)));
            l.add(new SignalConverter(new TimstampSignal(new Date())));
        }
        SignalConverterList list = new SignalConverterList(l);
        marshaller.marshallToJSON(list, System.out);
        System.out.println();
    }

    @Test
    public void testUnMarshal() throws Exception {
        JAXBContext context = JAXBContext.newInstance(SignalConverter.class);
        Unmarshaller um = context.createUnmarshaller();
        JSONUnmarshaller unmarshaller = JSONJAXBContext.getJSONUnmarshaller(um, context);
        String json = "{\"type\":\"ANALOG\",\"value\":\"100.0\"}";
        byte[] buf = json.getBytes();
        InputStream is = new ByteArrayInputStream(buf);
        SignalConverter s = unmarshaller.unmarshalFromJSON(is, SignalConverter.class);
        System.out.println(s.getSignal());
    }

    @Test
    public void testUnMarshalList() throws Exception {
        JAXBContext context = JAXBContext.newInstance(new Class[]{
                SignalConverter.class,
                SignalConverterList.class
        });
        Unmarshaller um = context.createUnmarshaller();
        JSONUnmarshaller unmarshaller = JSONJAXBContext.getJSONUnmarshaller(um, context);
        String json = "{\"list\":[" +
                "{\"type\":\"DIGITAL\",\"value\":\"false\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"0\"}," +
                "{\"type\":\"ANALOG\",\"value\":\"100.0\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.00\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-21 15:12:51.326\"}," +
                "{\"type\":\"DIGITAL\",\"value\":\"true\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"1\"}," +
                "{\"type\":\"ANALOG\",\"value\":\"101.0\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.01\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-21 15:12:51.343\"}," +
                "{\"type\":\"DIGITAL\",\"value\":\"true\"}," +
                "{\"type\":\"INTEGER\",\"value\":\"2\"}," +
                "{\"type\":\"ANALOG\",\"value\":\"102.0\"}," +
                "{\"type\":\"STRING\",\"value\":\"100.02\"}," +
                "{\"type\":\"TIMESTAMP\",\"value\":\"2017-06-21 15:12:51.343\"}]}\n";
        byte[] buf = json.getBytes();
        InputStream is = new ByteArrayInputStream(buf);
        SignalConverterList list = unmarshaller.unmarshalFromJSON(is, SignalConverterList.class);
        for (SignalConverter s : list.getList()) {
            System.out.println(s.getSignal());
        }
    }
}