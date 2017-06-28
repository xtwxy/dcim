package com.wincom.dcim.agentd.json;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Signal;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.PrimitiveMessageType;
import com.wincom.dcim.agentd.primitives.RequestMessage;
import com.wincom.dcim.agentd.primitives.SetSignalValues;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created by master on 6/21/17.
 */
@XmlRootElement(name = "request")
public class RequestConverter {
    @XmlElement
    private PrimitiveMessageType type;

    @XmlElement
    private List<String> keys;
    @XmlElement
    private List<SignalConverter> signals;

    private RequestConverter() {

    }

    public RequestConverter(RequestMessage request) {
        this.type = request.type();
        switch (type) {
            case GET_SIGNAL_VALUES_REQUEST:
                keys = new ArrayList<>();
                keys.addAll(((GetSignalValues.Request) request).getKeys());
                break;
            case SET_SIGNAL_VALUES_REQUEST:
                signals = new ArrayList<>();
                for (Map.Entry<String, Signal> entry : ((SetSignalValues.Request) request).getValues().entrySet()) {
                    signals.add(new SignalConverter(entry.getKey(), entry.getValue()));
                }
                break;
            default:
                break;
        }
    }

    public RequestMessage getRequest(HandlerContext sender) {
        switch (type) {
            case GET_SIGNAL_VALUES_REQUEST:
                Set<String> keySet = new HashSet<>();
                keySet.addAll(keys);
                return new GetSignalValues.Request(sender, keySet);
            case SET_SIGNAL_VALUES_REQUEST:
                Map<String, Signal> signalMap = new HashMap<>();
                for (SignalConverter sc : signals) {
                    signalMap.put(sc.getKey(), sc.getSignal());
                }
                return new SetSignalValues.Request(sender, signalMap);
            default:
                return null;
        }
    }
}
