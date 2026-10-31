package client.openkoa.modules.impl.movement;

import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.settings.impl.BooleanSetting;

public class NoDelay
extends Module {
    public static NoDelay INSTANCE;
    public final BooleanSetting fastDig = new BooleanSetting("No Jump Delay", true);

    public NoDelay() {
        super("NoDelay", Category.MOVEMENT);
        INSTANCE = this;
    }
}