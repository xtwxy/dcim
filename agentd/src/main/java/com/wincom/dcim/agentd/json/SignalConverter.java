package com.wincom.dcim.agentd.json;

import com.wincom.dcim.agentd.domain.Signal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by master on 6/21/17.
 */
@XmlRootElement(name = "signal")
public class SignalConverter {
    @XmlElement
    private Signal.Type type;
    @XmlElement
    private String value;
    @XmlElement
    private String key;

    private SignalConverter() {

    }

    public SignalConverter(Signal signal) {
        this.type = signal.type();
        this.value = signal.stringValue();
    }

    public SignalConverter(String key, Signal signal) {
        this.key = key;
        this.type = signal.type();
        this.value = signal.stringValue();
    }

    public String getKey() {
        return key;
    }

    public Signal getSignal() {
        return type.create(value);
    }
}
