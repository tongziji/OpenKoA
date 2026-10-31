package client.openkoa.modules.impl.world;

import java.nio.file.Path;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import client.openkoa.OpenKoAClient;
import client.openkoa.event.EventTarget;
import client.openkoa.event.impl.PacketEvent;
import client.openkoa.event.impl.TickEvent;
import client.openkoa.modules.Category;
import client.openkoa.modules.Module;
import client.openkoa.protocol.heypixel.HeyPixelId1Wiring;
import client.openkoa.protocol.heypixel.HeyPixelProtocolRuntime;
import client.openkoa.settings.impl.BooleanSetting;
import client.openkoa.settings.impl.ModeSetting;

public final class Protocol extends Module {
    private final HeyPixelProtocolRuntime runtime = new HeyPixelProtocolRuntime(
        mc, Path.of(OpenKoAClient.configDir));
    public final ModeSetting enabledHosts = new ModeSetting("Hosts", "pc.bjdmc.net,*.bjdmc.net").withDefault("pc.bjdmc.net,*.bjdmc.net");
    public final BooleanSetting traceLogger = new BooleanSetting("Trace Logger", false);
    public final BooleanSetting observeOnly = new BooleanSetting("Observe Only", false);
    public final BooleanSetting allowLiveSend = new BooleanSetting("Allow Live Send", true);
    public final BooleanSetting strictProviderGate = new BooleanSetting("Strict Provider Gate", false);

    public Protocol() {
        super("Protocol", Category.WORLD);
    }

    @Override
    public void onEnable() {
        HeyPixelId1Wiring.configure(runtime);
        updateRuntimeSettings();
        runtime.start();
    }

    @Override
    public void onDisable() {
        runtime.stop();
    }

    @Override
    public String getSuffix() {
        return runtime.isActiveForCurrentServer() ? "HeyPixel" : "Idle";
    }

    @EventTarget
    public void onTick(TickEvent event) {
        updateRuntimeSettings();
        runtime.tick();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!event.isIncoming()) return;
        if (event.getPacket() instanceof ClientboundCustomPayloadPacket payload) {
            runtime.handle(payload);
        }
    }

    public HeyPixelProtocolRuntime getRuntime() {
        return runtime;
    }

    private void updateRuntimeSettings() {
        runtime.configure(
            enabledHosts.getValue(),
            traceLogger.getValue(),
            observeOnly.getValue(),
            allowLiveSend.getValue(),
            strictProviderGate.getValue()
        );
    }
}
