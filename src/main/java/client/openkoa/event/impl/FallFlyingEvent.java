package client.openkoa.event.impl;

import lombok.*;
import client.openkoa.event.EventMarker;

@Data
@AllArgsConstructor
public class FallFlyingEvent
implements EventMarker {
    @Getter @Setter
    private float pitch;
}