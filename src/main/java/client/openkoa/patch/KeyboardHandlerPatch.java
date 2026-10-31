package client.openkoa.patch;

import asm.patchify.annotation.Inject;
import asm.patchify.annotation.Patch;
import net.minecraft.client.KeyboardHandler;
import client.openkoa.OpenKoAClient;
import client.openkoa.event.impl.KeyEvent;

@Patch(KeyboardHandler.class)
public class KeyboardHandlerPatch {
    @Inject(method = "keyPress", desc = "(JIIII)V")
    public static void onKeyPress(KeyboardHandler handler, long window, int keyCode, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (handler == null || !OpenKoAClient.isReady()) return;
        KeyEvent event = new KeyEvent(keyCode, action != 0);
        OpenKoAClient.getInstance().getEventBus().call(event);
        if (event.isCancelled()) {
            callbackInfo.cancelled = true;
        }
    }
}
