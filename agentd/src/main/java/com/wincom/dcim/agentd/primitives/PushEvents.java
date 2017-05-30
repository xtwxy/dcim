package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.domain.Event;

import java.util.ArrayList;
import java.util.List;

public final class PushEvents extends Message.Adapter {

    private List<Event> events;

    public PushEvents() {
        this.events = new ArrayList<>();
    }

    public PushEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
