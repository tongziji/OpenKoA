package client.openkoa.event.impl;

import lombok.*;
import client.openkoa.event.EventMarker;

@AllArgsConstructor
@Data
public class UseItemRayTraceEvent
implements EventMarker {
    @Getter @Setter
    private float yaw;
    @Getter @Setter
    private float pitch;
}