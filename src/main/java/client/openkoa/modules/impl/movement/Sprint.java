package client.openkoa.modules.impl.movement;

import client.openkoa.event.impl.MotionEvent;
import client.openkoa.event.impl.SprintEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.modules.impl.combat.KillAura;
import client.openkoa.modules.impl.player.InventoryManager;
import client.openkoa.modules.impl.player.Stuck;
import client.openkoa.event.EventTarget;

public class Sprint
extends Module {

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        this.setEnabled(true);
    }

    @EventTarget(value = 0)
    public void onSprint(SprintEvent sprintEvent) {
        if (mc.player == null || InventoryManager.isPerformingAction) {
            return;
        }
        if (this.shouldHoldSprint()) {
            mc.options.keySprint.setDown(false);
            mc.player.setSprinting(false);
            return;
        }
        mc.options.toggleSprint().set(false);
        mc.options.keySprint.setDown(true);
        if (mc.player.zza != 0) {
            mc.player.setSprinting(true);
        }
    }

    private boolean shouldHoldSprint() {
        if (Stuck.INSTANCE != null && Stuck.INSTANCE.isEnabled()) {
            return true;
        }
        return KillAura.INSTANCE != null && KillAura.INSTANCE.canCritUnderVelocity();
    }
}