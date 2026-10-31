package client.openkoa.modules.impl.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import client.openkoa.gui.NewClickGui;
import client.openkoa.gui.OldClickGui;
import client.openkoa.gui.PanelClickGui;
import client.openkoa.gui.neverlose.NeverloseClickGui;
import client.openkoa.gui.openkoa.OpenKoAClickGui;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.settings.impl.ModeSetting;

public class ClickGuiModule
extends Module {
    public static final Logger LOGGER = LogManager.getLogger(ClickGuiModule.class);
    public final ModeSetting styleSetting = new ModeSetting("Mode", "Old", "Panel", "New", "Neverlose", "OpenKoA").withDefault("OpenKoA");
    public final ModeSetting openkoaTheme = new ModeSetting("Theme", "Light", "Dark").withDefault("Light")
            .withVisibility(() -> this.styleSetting.is("OpenKoA"));

    public ClickGuiModule() {
        super("ClickGui", Category.RENDER, 344);
    }

    @Override
    protected void onEnable() {
        try {
            if (this.styleSetting.is("Old")) {
                mc.setScreen(new OldClickGui());
            } else if (this.styleSetting.is("Panel")) {
                mc.setScreen(PanelClickGui.panelClickGui);
            } else if (this.styleSetting.is("Neverlose")) {
                mc.setScreen(NeverloseClickGui.instance);
            } else if (this.styleSetting.is("OpenKoA")) {
                mc.setScreen(OpenKoAClickGui.instance);
            } else {
                mc.setScreen(new NewClickGui());
            }
            LOGGER.info("ClickGUI opened successfully");
        } catch (Exception exception) {
            LOGGER.error("Error opening ClickGUI", exception);
        } finally {
            this.setEnabled(false);
        }
    }
}
