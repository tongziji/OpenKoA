package client.openkoa.modules.impl.player.helper;

import lombok.Getter;
import client.openkoa.ClientBase;
import client.openkoa.event.impl.MotionEvent;
import client.openkoa.event.impl.PreMotionEvent;
import client.openkoa.event.impl.RenderEvent;
import client.openkoa.event.impl.TickEvent;
import client.openkoa.utils.rotation.Rotation;

public abstract class HelperBase
extends ClientBase {
    @Getter
    private final String name;

    public HelperBase(String string) {
        this.name = string;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick(TickEvent tickEvent) {
    }

    public void onMotion(MotionEvent motionEvent) {
    }

    public void onRender(RenderEvent renderEvent) {
    }

    public void onPreMotion(PreMotionEvent preMotionEvent) {
    }

    public boolean isActive() {
        return false;
    }

    public Rotation getTargetRotation() {
        return null;
    }

    }