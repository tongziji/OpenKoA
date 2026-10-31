package client.openkoa.modules.impl.render;

import client.openkoa.modules.Category;
import client.openkoa.modules.Module;

public class NoHurtCam
extends Module {
    public static NoHurtCam INSTANCE;
    public NoHurtCam() {
        super("NoHurtCam", Category.RENDER);
        INSTANCE = this;
    }
}