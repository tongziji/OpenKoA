package client.openkoa.modules.impl.movement.speed.impl;

import client.openkoa.event.EventTarget;
import client.openkoa.event.impl.MotionEvent;
import client.openkoa.modules.impl.movement.SpeedModule;
import client.openkoa.modules.impl.movement.speed.SpeedMode;
import client.openkoa.settings.impl.NumberSetting;
import client.openkoa.utils.game.MovementUtil;

public class SpeedOnGround extends SpeedMode {
    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 1.0, 5.0, 0.1, () -> SpeedModule.is(this));

    public SpeedOnGround() {
        super("OnGround");
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (!event.isPre()) return;
        if (mc.player == null || mc.level == null) return;
        if (MovementUtil.isMoving() && mc.player.onGround()) {
            MovementUtil.strafeForward(speed.getValue().doubleValue() / 12);
        }
    }
}
