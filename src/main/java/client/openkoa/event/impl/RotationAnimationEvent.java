package client.openkoa.event.impl;

import lombok.*;
import client.openkoa.event.EventMarker;

@ToString
@Data
@AllArgsConstructor
public class RotationAnimationEvent
implements EventMarker {
    @Getter @Setter
    private float yaw, lastYaw;
    @Getter @Setter
    private float pitch, lastPitch;
}