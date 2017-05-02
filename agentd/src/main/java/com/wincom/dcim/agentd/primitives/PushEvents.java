package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.domain.Event;

import java.util.ArrayList;
import java.util.List;

public class PushEvents {

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

    public void apply(Handler handler) {
        handler.apply(this);
    }
}
