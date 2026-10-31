package client.openkoa.event.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import client.openkoa.event.EventMarker;

@Data
@AllArgsConstructor
public class JumpMarkerEvent
implements EventMarker {
    private float yaw;
}