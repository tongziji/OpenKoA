package client.openkoa.modules.impl.render;

import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.settings.impl.NumberSetting;
import client.openkoa.utils.misc.Triple;
import client.openkoa.utils.misc.TripleProvider;

public class FullBright
extends Module
implements TripleProvider {
    public static FullBright INSTANCE;
    public final NumberSetting brightnessSetting = new NumberSetting("Brightness", 90, 0.0f, 100.0f, 1.0f);

    public FullBright() {
        super("FullBright", Category.RENDER);
        INSTANCE = this;
    }

    @Override
    public Triple getTriple() {
        if (this.isEnabled()) {
            return new Triple(this.getName(), String.valueOf(this.brightnessSetting.getValue().intValue()), true);
        }
        return null;
    }
}