package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Event;
import com.wincom.dcim.agentd.messages.Message;

import java.util.ArrayList;
import java.util.List;

public final class PushEvents extends Message.Adapter {

    private List<Event> events;

    public PushEvents(HandlerContext sender) {
        super(sender);
        this.events = new ArrayList<>();
    }

    public PushEvents(HandlerContext sender, List<Event> events) {
        super(sender);
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
