package client.openkoa.modules.impl.render;

import client.openkoa.event.impl.PacketEvent;
import client.openkoa.event.impl.Render2DEvent;
import client.openkoa.event.impl.RenderEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.modules.impl.render.nametag.NameTagStyle;
import client.openkoa.settings.impl.BooleanSetting;
import client.openkoa.settings.impl.ModeSetting;
import client.openkoa.settings.impl.NumberSetting;
import client.openkoa.event.EventTarget;

public class NameTags
extends Module {
    public static NameTags INSTANCE;
    public final ModeSetting styleSetting = new ModeSetting("Style", "Opal", "Simple", "Naven").withDefault("Simple");
    public final NumberSetting scaleSetting = new NumberSetting("Scale", 0.6, 0.1, 1.0, 0.01);
    public final NumberSetting distanceSetting = new NumberSetting("Max Distance", 128, 8.0, 256.0, 1.0);
    public final BooleanSetting showHealthSetting = new BooleanSetting("Invisibles", true);
    public final BooleanSetting showArmorSetting = new BooleanSetting("Show Artifacts", false);
    public final BooleanSetting showPingSetting = new BooleanSetting("Hide Teammates", false);

    public NameTags() {
        super("NameTags", Category.RENDER);
        INSTANCE = this;
        NameTagStyle.registerStyles();
    }

    @Override
    public void onEnable() {
        NameTagStyle nameTagStyle = NameTagStyle.getByName(this.styleSetting.getValue());
        if (nameTagStyle != null) {
            nameTagStyle.onEnable();
        }
    }

    @Override
    public void onDisable() {
        NameTagStyle nameTagStyle = NameTagStyle.getByName(this.styleSetting.getValue());
        if (nameTagStyle != null) {
            nameTagStyle.onDisable();
        }
    }

    @EventTarget
    public void onRender(RenderEvent renderEvent) {
        NameTagStyle nameTagStyle = NameTagStyle.getByName(this.styleSetting.getValue());
        if (nameTagStyle != null) {
            nameTagStyle.onRender(renderEvent);
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        NameTagStyle nameTagStyle = NameTagStyle.getByName(this.styleSetting.getValue());
        if (nameTagStyle != null) {
            nameTagStyle.onRender2D(render2DEvent);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        NameTagStyle nameTagStyle = NameTagStyle.getByName(this.styleSetting.getValue());
        if (nameTagStyle != null) {
            nameTagStyle.onPacket(packetEvent);
        }
    }
}