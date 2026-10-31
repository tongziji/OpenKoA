package client.openkoa.event.impl;

import client.openkoa.event.EventMarker;
import client.openkoa.modules.Module;

public record ModuleToggleEvent(Module module, boolean enabled) implements EventMarker {
}
