package client.openkoa.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import client.openkoa.ClientBase;
import client.openkoa.OpenKoAClient;
import client.openkoa.event.impl.GlRenderEvent;
import client.openkoa.event.impl.Render2DEvent;
import client.openkoa.event.impl.TickEvent;
import client.openkoa.gui.IntroAnimation;
import client.openkoa.hud.HudElement;
import client.openkoa.hud.KeyBindsHud;
import client.openkoa.hud.LieDetector;
import client.openkoa.hud.MusicPlayerHud;
import client.openkoa.hud.ModuleListHud;
import client.openkoa.hud.NotificationHud;
import client.openkoa.hud.PlayerListHud;
import client.openkoa.hud.PotionEffectsHud;
import client.openkoa.hud.ScoreboardHud;
import client.openkoa.hud.TargetHud;
import client.openkoa.modules.impl.render.Armor;
import client.openkoa.event.EventTarget;

public class HudManager {
    private final Map<String, HudElement> hudElements = new HashMap<>();

    public HudManager() {
        this.init();
    }

    public void init() {
        this.registerHudElement(new TargetHud());
        this.registerHudElement(new KeyBindsHud());
        this.registerHudElement(new ModuleListHud());
        this.registerHudElement(new PlayerListHud());
        this.registerHudElement(new PotionEffectsHud());
        this.registerHudElement(new LieDetector());
        this.registerHudElement(new MusicPlayerHud());
        this.registerHudElement(new NotificationHud());
        this.registerHudElement(new Armor());
        this.registerHudElement(new ScoreboardHud());
    }

    private void registerHudElement(HudElement hudElement) {
        OpenKoAClient.getInstance().getModuleManager().register(hudElement);
        this.hudElements.put(hudElement.getClass().getSimpleName(), hudElement);
    }

    public <T extends HudElement> T getHudElement(Class<T> clazz) {
        return clazz.cast(this.hudElements.get(clazz.getSimpleName()));
    }

    public HudElement getHudElementByName(String string) {
        return this.hudElements.values().stream().filter(hudElement -> hudElement.getName().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public Collection<HudElement> getHudElements() {
        return this.hudElements.values();
    }

    @EventTarget
    public void onTick(TickEvent tickEvent) {
        if (ClientBase.mc.screen == null) {
            try {
                for (HudElement hudElement : OpenKoAClient.getInstance().getHudManager().getHudElements()) {
                    hudElement.stopDragging();
                }
            } catch (Exception exception) {
                ClientBase.logger.error(exception);
                ClientBase.logger.error(exception.getMessage());
            }
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        if (IntroAnimation.isRunning()) {
            return;
        }
        for (HudElement hudElement : this.getHudElements()) {
            if (!hudElement.isEnabled()) continue;
            hudElement.onRender2D(render2DEvent, hudElement.getX(), hudElement.getY());
        }
    }

    @EventTarget
    public void onGlRender(GlRenderEvent glRenderEvent) {
        if (IntroAnimation.isRunning()) {
            return;
        }
        for (HudElement hudElement : this.getHudElements()) {
            if (!hudElement.isEnabled()) continue;
            hudElement.onGlRender(glRenderEvent, hudElement.getX(), hudElement.getY());
        }
    }
}