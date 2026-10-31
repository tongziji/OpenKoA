package client.openkoa.event.impl;

import client.openkoa.event.EventMarker;
import client.openkoa.event.Prioritized;

public abstract class PrioritizedEvent
implements Prioritized,
EventMarker {
    private final byte priority;

    protected PrioritizedEvent(byte by) {
        this.priority = by;
    }

    @Override
    public byte getPriority() {
        return this.priority;
    }
}