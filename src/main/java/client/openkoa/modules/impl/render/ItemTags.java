package client.openkoa.modules.impl.render;

import client.openkoa.modules.Category;
import client.openkoa.modules.Module;

public class ItemTags extends Module {
    public static ItemTags INSTANCE;

    public ItemTags() {
        super("ItemTags", Category.RENDER);
        INSTANCE = this;
    }
}
