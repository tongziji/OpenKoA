package client.openkoa.modules.impl.movement;

import client.openkoa.OpenKoAClient;
import client.openkoa.event.EventTarget;
import client.openkoa.event.impl.GameTickEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.modules.impl.movement.speed.SpeedMode;
import client.openkoa.modules.impl.movement.speed.impl.SpeedMotion;
import client.openkoa.modules.impl.movement.speed.impl.SpeedOnGround;
import client.openkoa.settings.impl.ModeSetting;

import java.util.List;

public class SpeedModule extends Module {
    private final List<SpeedMode> modes = List.of(
            new SpeedOnGround(),
            new SpeedMotion()
    );
    public final ModeSetting mode;
    private static SpeedMode activeMode;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT);
        mode = new ModeSetting("Mode", "OnGround", "Motion").withDefault("Motion");
        activeMode = modes.get(0);
        activeMode.setActive(true);
        modes.forEach(speedMode -> getSettings().addAll(speedMode.getValues()));
    }

    @Override
    public void onEnable() {
        activeMode = getActiveMode();
        activeMode.setActive(true);
        OpenKoAClient.getInstance().getEventBus().register(activeMode);
        activeMode.onEnable();
    }

    @Override
    public void onDisable() {
        activeMode.onDisable();
        OpenKoAClient.getInstance().getEventBus().unregister(activeMode);
        activeMode.setActive(false);
    }

    @EventTarget
    public void onGameTick(GameTickEvent event) {
        SpeedMode selected = getActiveMode();
        if (activeMode != selected) {
            activeMode.onDisable();
            OpenKoAClient.getInstance().getEventBus().unregister(activeMode);
            activeMode.setActive(false);
            activeMode = selected;
            activeMode.setActive(true);
            OpenKoAClient.getInstance().getEventBus().register(activeMode);
            activeMode.onEnable();
        }
    }

    private SpeedMode getActiveMode() {
        for (SpeedMode speedMode : modes) {
            if (mode.is(speedMode.getName())) {
                return speedMode;
            }
        }
        return modes.get(0);
    }

    public static boolean is(SpeedMode mode) {
        return activeMode == mode;
    }
}
