package client.openkoa.event;

import client.openkoa.event.Cancellable;
import client.openkoa.event.EventMarker;

public abstract class Event
implements Cancellable,
EventMarker {
    public boolean cancelled;

    protected Event() {
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}