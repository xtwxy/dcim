package com.wincom.dcim.agentd.domain;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Created by master on 6/21/17.
 */
@XmlRootElement(name = "SignalConverterList")
public class SignalConverterList {

    @XmlElement(name = "list")
    private List<SignalConverter> list;

    private SignalConverterList() {

    }

    public SignalConverterList(List<SignalConverter> list) {
        this.list = list;
    }

    public List<SignalConverter> getList() {
        return list;
    }
}
