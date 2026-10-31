package client.openkoa.modules.impl.render;

import net.minecraft.core.BlockPos;
import client.openkoa.event.EventTarget;
import client.openkoa.event.impl.RenderEvent;
import client.openkoa.event.impl.WorldChangeEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.render.terrain.TerrainLodRenderer;
import client.openkoa.settings.impl.NumberSetting;

public class TerrainLOD extends Module {
    public static TerrainLOD INSTANCE;

    private final NumberSetting start = new NumberSetting("Start", 128, 64, 512, 8);

    public TerrainLOD() {
        super("TerrainLOD", Category.RENDER);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        TerrainLodRenderer.reset();
        super.onDisable();
    }

    @EventTarget
    public void onWorldChange(WorldChangeEvent event) {
        TerrainLodRenderer.reset();
    }

    @EventTarget
    public void onRender(RenderEvent event) {
        TerrainLodRenderer.render(event.poseStack(), this.start.getValue().doubleValue());
    }

    public static double startDistance() {
        return INSTANCE == null ? 0.0 : INSTANCE.start.getValue().doubleValue();
    }

    public static boolean covers(BlockPos origin, double distanceSq) {
        return INSTANCE != null
                && INSTANCE.isEnabled()
                && TerrainLodRenderer.shouldReplace(origin, distanceSq, startDistance());
    }
}
