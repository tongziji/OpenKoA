package client.openkoa.modules.impl.movement;

import client.openkoa.event.impl.SneakEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.event.EventTarget;

public class NoPush
extends Module {
    public NoPush() {
        super("NoPush", Category.MOVEMENT);
    }

    @EventTarget
    public void onSneak(SneakEvent sneakEvent) {
        if (!FireballBlink.INSTANCE.isEnabled()) {
            sneakEvent.setCancelled(true);
        }
    }
}